package Connectivity;

import Entities.*;
import enums.Disability;
import enums.MaritalStatus;
import enums.Retiree;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static java.lang.Thread.sleep;

public class Server extends Application implements TCPConnectionListener {
    private volatile static int serverState = 1;
    private volatile static String log = "";
    private volatile static ObservableList<TCPConnection> connections = FXCollections.observableArrayList();
    private volatile static ServerSocket serverSocket;
    private final DatabaseConnection connDB;
    private double xOffset = 0;
    private double yOffset = 0;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private ObservableList<Group> groupsData = FXCollections.observableArrayList();
    private ObservableList<Lesson> lessonsData = FXCollections.observableArrayList();
    private ObservableList<Student> studentsData = FXCollections.observableArrayList();
    private ObservableList<Subject> subjectsData = FXCollections.observableArrayList();
    private ObservableList<Teacher> teachersData = FXCollections.observableArrayList();
    private ObservableList<TeacherSubject> teachersSubjectsData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane primaryAnchorPane;

    @FXML
    private AnchorPane title;

    @FXML
    private Button hideButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button exitButton;

    @FXML
    private AnchorPane workPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextArea textAreaLog = new TextArea();

    @FXML
    private MenuButton serverOnOffMenuButton;
    @FXML
    private MenuItem serverOnMenuItem;
    @FXML
    private MenuItem serverOffMenuItem;
    @FXML
    private MenuItem serverStopMenuItem;

    @FXML
    private Button languageButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;

    @FXML
    private TableView<TCPConnection> connectionsTable;
    private Label emptyLabel;

    @FXML
    private TableColumn<TCPConnection, String> ipColumn;
    @FXML
    private TableColumn<TCPConnection, String> portColumn;
    @FXML
    private TableColumn<TCPConnection, String> userColumn;

    @FXML
    private Tooltip serverToolTip;

    @FXML
    private Label serverLabel;
    @FXML
    private Label titleLabel;
    private String currentLanguage;

    public Server() {
        System.out.println("Server's running...");
        connDB =
                new DatabaseConnection("jdbc:mysql://localhost:3306/flsdb?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
                        "&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow", "root", "root");

        try {
            if (serverSocket == null) {
                serverSocket = new ServerSocket(8000);
                serverSocket.setSoTimeout(1000);
            }
        } catch (IOException e) {
            System.out.println("Another instance of server is running!!!");
            System.exit(0);
        }

        try {
            initUsersData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    void initialize() {
        try {
            initGroupsData();
            initSubjectsData();
            initTeachersData();
            initStudentsData();
            initTeachersSubjectsData();
            initLessonsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        serverToolTip.setShowDelay(Duration.seconds(0));
        serverToolTip.setShowDuration(Duration.INDEFINITE);
        serverToolTip.setHideOnEscape(true);
        serverToolTip.setFont(Font.font("Monospaced", 13));
        serverToolTip.setText("ON - server is working and accepting new connections \n" +
                "STOP - server is working and does not accept new connections\n" +
                "OFF - server is not working");
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        portColumn.setCellValueFactory(new PropertyValueFactory<>("port"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        textAreaLog.getStyleClass().add("textAreaLog");
        mainPane.getStyleClass().add("mainPane");
        title.getStyleClass().add("title");
        workPane.getStyleClass().add("workPane");
        primaryAnchorPane.getStylesheets().add("CSS/DarkTheme.css");
        primaryAnchorPane.setVisible(true);
        hideButton.getStyleClass().add("hideButton");
        minimizeButton.getStyleClass().add("minimizeButton");
        exitButton.getStyleClass().add("exitButton");
        hideButton.setOnAction(actionEvent -> {
            Stage stage2 = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage2.setIconified(true);
        });
        minimizeButton.setOnAction(actionEvent -> minimize());
        exitButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            System.exit(0);
        });
        languageButton.setOnAction(event -> translate());
        languageButton.getStyleClass().add("languageButton");
        saveButton.setOnAction(event -> saveLog());
        clearButton.setOnAction(event -> {
            textAreaLog.setText("");
            log = "";
        });
        emptyLabel = new Label("No connections");
        try {
            titleLabel.setText(Inet4Address.getLocalHost().getHostAddress() + ":" + serverSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverOnMenuItem.setOnAction(event -> {
            if (serverState != 1) {
                serverOnOffMenuButton.setText(serverOnMenuItem.getText());
                serverState = 1;
                try {
                    serverSocket = new ServerSocket(8000);
                    titleLabel.setText(Inet4Address.getLocalHost().getHostAddress() + ":" + serverSocket.getLocalPort());
                    serverSocket.setSoTimeout(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Thread(this::listen).start();
            }
        });
        serverStopMenuItem.setOnAction(event -> {
            if (serverState != 0) {
                serverOnOffMenuButton.setText(serverStopMenuItem.getText());
                serverState = 0;
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverOffMenuItem.setOnAction(event -> {
            if (serverState != -1) {
                serverOnOffMenuButton.setText(serverOffMenuItem.getText());
                Platform.runLater(() -> {

                    for (int i = 0; i < connections.size(); ) {
                        connections.get(i).disconnect();
                    }

                    System.out.println("SIZE = " + connections.size());

                    for (TCPConnection c : connections)
                        System.out.println(c);

                });
                serverState = -1;
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addDeleteButton();
        connectionsTable.setPlaceholder(emptyLabel);
        connectionsTable.setItems(connections);
        new Thread(() -> {
            while (true) {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateLog();
            }
        }).start();
        currentLanguage = "English";
    }

    public void translate() {
        if (currentLanguage.equals("English")) {
            currentLanguage = "Russian";
            Platform.runLater(() -> {
                languageButton.setStyle("-fx-background-image: url(assets/russian.png)");
                saveButton.setText("Сохранить");
                clearButton.setText("Отчистить");
                serverLabel.setText("Сервер:");
                userColumn.setText("Пользователь");
                portColumn.setText("Порт");
                emptyLabel.setText("Нет подключений");
                serverToolTip.setText("ВКЛ - сервер работает и принимает новые подключения\n" +
                        "СТОП - сервер работает и не принимает новые подключения\n" +
                        "ВЫКЛ - сервер не работает");
                serverOnMenuItem.setText("ВКЛ");
                serverOffMenuItem.setText("ВЫКЛ");
                serverStopMenuItem.setText("СТОП");
                if (serverOnOffMenuButton.getText().equals("ON"))
                    serverOnOffMenuButton.setText("ВКЛ");
                else
                    serverOnOffMenuButton.setText("ВЫКЛ");
            });

        } else {
            currentLanguage = "English";
            Platform.runLater(() -> {
                languageButton.setStyle("-fx-background-image: url(assets/english.png)");
                saveButton.setText("Save log");
                clearButton.setText("Clear log");
                serverLabel.setText("Server:");
                userColumn.setText("Current user");
                portColumn.setText("Port");
                emptyLabel.setText("No connections");

                serverToolTip.setText("ON - server is working and accepting new connections \n" +
                        "STOP - server is working and does not accept new connections\n" +
                        "OFF - server is not working");
                serverOnMenuItem.setText("ON");
                serverOffMenuItem.setText("OFF");
                serverStopMenuItem.setText("STOP");
                if (serverOnOffMenuButton.getText().equals("ВКЛ"))
                    serverOnOffMenuButton.setText("ON");
                else
                    serverOnOffMenuButton.setText("OFF");
            });
        }
        connectionsTable.refresh();
    }

    public void listen() {
        System.out.println("listening");
        while (serverState == 1) {
            try {
                TCPConnection newConn = new TCPConnection(this, serverSocket.accept());
                if (serverState == 1)
                    connections.add(newConn);
                else {
                    newConn.disconnect();
                    log += getCurrentDateTime() + newConn + " REJECTED (STOP mode)\n";
                }
            } catch (IOException e) {

            }
        }
    }

    public void saveLog() {
        try {
            ZonedDateTime zdt = ZonedDateTime.now();
            String year = String.valueOf(zdt.getYear());
            String month = String.valueOf(zdt.getMonthValue());
            String day = String.valueOf(zdt.getDayOfMonth());
            String hour = String.valueOf(zdt.getHour());
            String minute = String.valueOf(zdt.getMinute());
            String second = String.valueOf(zdt.getSecond());
            String path = "log-" + year + month + day + "(" + hour + "h" + minute + "m" + second + "s).txt";
            File savedLog = new File(path);
            FileWriter lastConfigWriter = new FileWriter(savedLog, false);
            if (!savedLog.exists())
                if (savedLog.createNewFile())
                    System.out.println(path + " created.");

            lastConfigWriter.write(log);
            log += getCurrentDateTime() + " log saved to " + path + "\n";
            lastConfigWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        Platform.runLater(() -> {
            log += getCurrentDateTime() + " " + tcpConnection + " CONNECTED\n";
        });
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        if (value != null) {
            System.out.println(value);
            if (value.equals("init")) {
                try {
                    initUsersData();
                    initGroupsData();
                    initLessonsData();
                    initStudentsData();
                    initSubjectsData();
                    initTeachersData();
                    initTeachersSubjectsData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                tcpConnection.sendString(usersData.size() + " USERS:");
                for (User u : usersData)
                    tcpConnection.sendString(u.toString());

                tcpConnection.sendString(teachersData.size() + " TEACHERS:");
                for (Teacher t : teachersData)
                    tcpConnection.sendString(t.toString());

                tcpConnection.sendString(studentsData.size() + " STUDENTS:");
                for (Student s : studentsData)
                    tcpConnection.sendString(s.toString());

                tcpConnection.sendString(lessonsData.size() + " LESSONS:");
                for (Lesson l : lessonsData)
                    tcpConnection.sendString(l.toString());

                tcpConnection.sendString(subjectsData.size() + " SUBJECTS:");
                for (Subject sb : subjectsData)
                    tcpConnection.sendString(sb.toString());

                tcpConnection.sendString(groupsData.size() + " GROUPS:");
                for (Group g : groupsData)
                    tcpConnection.sendString(g.toString());

                tcpConnection.sendString(teachersSubjectsData.size() + " TEACHERSSUBJECTS:");
                for (TeacherSubject ts : teachersSubjectsData)
                    tcpConnection.sendString(ts.toString());

                tcpConnection.sendString("END");

            } else {
                String[] vals = value.split("\\|");
                switch (vals[0]) {
                    case "User":
                        log(tcpConnection, value);
                        for (User u : usersData)
                            if (u.getId() == Integer.parseInt(vals[2])) {
                                try {
                                    u.set(connDB, vals[1], vals[3]);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        break;
                    case "Lesson":
                        log(tcpConnection, value);
                        for (Lesson l : lessonsData)
                            if (l.getId() == Integer.parseInt(vals[2])) {
                                    l.set(connDB, vals[1], vals[3]);
                                break;
                            }
                        break;
                    case "TeacherSubject":
                        log(tcpConnection, value);
                        for (TeacherSubject ts : teachersSubjectsData)
                            if (ts.getId() == Integer.parseInt(vals[2])) {
                                ts.set(connDB, vals[1], vals[3]);
                                break;
                            }
                        break;
                    case "Group":
                        log(tcpConnection, value);
                        for (Group g : groupsData)
                            if (g.getId() == Integer.parseInt(vals[2])) {
                                g.set(connDB, vals[1], vals[3]);
                                break;
                            }
                        break;
                    case "Student":
                        log(tcpConnection, value);
                        for (Student s : studentsData)
                            if (s.getId() == Integer.parseInt(vals[2])) {
                                s.set(connDB, vals[1], vals[3]);
                                break;
                            }
                        break;
                    case "Teacher":
                        log(tcpConnection, value);
                        for (Teacher t : teachersData)
                            if (t.getId() == Integer.parseInt(vals[2])) {
                                t.set(connDB, vals[1], vals[3]);
                                break;
                            }
                        break;
                    case "Subject":
                        log(tcpConnection, value);
                        for (Subject sb : subjectsData)
                            if (sb.getId() == Integer.parseInt(vals[2])) {
                                sb.set(connDB, vals[1], vals[3]);
                                break;
                            }
                        break;
                    case "addUser":
                        log(tcpConnection, value);
                        System.out.println(value.substring(8));
                        addUser(value.substring(8));
                        break;
                    case "addLesson":
                        log(tcpConnection, value);
                        System.out.println(value.substring(10));
                        addLesson(value.substring(10));
                        break;
                    case "addTeacherSubject":
                        log(tcpConnection, value);
                        System.out.println(value.substring(18));
                        addTeacherSubject(value.substring(18));
                        break;
                    case "addGroup":
                        log(tcpConnection, value);
                        System.out.println(value.substring(9));
                        addGroup(value.substring(9));
                        break;
                    case "addStudent":
                        log(tcpConnection, value);
                        System.out.println(value.substring(11));
                        addStudent(value.substring(11));
                        break;
                    case "addTeacher":
                        log(tcpConnection, value);
                        System.out.println(value.substring(11));
                        addTeacher(value.substring(11));
                        break;
                    case "addSubject":
                        log(tcpConnection, value);
                        System.out.println(value.substring(11));
                        addSubject(value.substring(11));
                        break;
                    case "changeAccountData":
                        log(tcpConnection, value);
                        System.out.println(value.substring(18));
                        changeAccountData(vals[1], vals[2], vals[3], vals[4]);
                        break;
                    case "deleteAllUsers":
                        log(tcpConnection, value);
                        deleteAllUsers();
                        break;
                    case "setCurrentUser":
                        System.out.println(value.substring(15));
                        if (vals[1].equals("null"))
                            log +=
                                    getCurrentDateTime() + " " + tcpConnection + " \'" + tcpConnection.getUsername() + "\' logged out\n";
                        else
                            log += getCurrentDateTime() + " " + tcpConnection + " \'" + vals[1] + "\' logged in\n";
                        tcpConnection.setUsername(vals[1]);


                        break;
                    default:
                        log(tcpConnection, value);
                }
            }
        }
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        Platform.runLater(() -> {
            log += getCurrentDateTime() + " " + tcpConnection + " DISCONNECTED\n";
        });
        connections.remove(tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void initUsersData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            Statement statement2 = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM users");
            usersData.clear();

            System.out.println();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setAccessMode(resultSet.getInt("access_mode"));
                user.setUsername(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setEMail(resultSet.getString("email"));
                ResultSet resultSetConfigs = statement2.executeQuery("SELECT * FROM user_configs");
                while (resultSetConfigs.next())
                    if (resultSetConfigs.getInt("userId") == user.getId()) {
                        user.setTheme(resultSetConfigs.getString("theme"));
                        user.setLanguage(resultSetConfigs.getString("language"));
                    }
                usersData.add(user);
                System.out.println(user);
            }
        }
    }

    private void initGroupsData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM `group`");
            groupsData.clear();

            System.out.println();
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("id"));
                group.setLevel(resultSet.getString("level"));
                groupsData.add(group);
                System.out.println(group);
            }
        }
    }

    private void initSubjectsData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM subject");
            subjectsData.clear();

            System.out.println();
            while (resultSet.next()) {
                Subject subject = new Subject();
                subject.setId(resultSet.getInt("id"));
                subject.setName(resultSet.getString("name"));
                subject.setHours(resultSet.getInt("hours"));
                subjectsData.add(subject);
                System.out.println(subject);
            }
        }
    }

    private void initTeachersData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM teacher");
            teachersData.clear();

            System.out.println();
            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(resultSet.getInt("id"));
                teacher.setName(resultSet.getString("name"));
                teacher.setSurname(resultSet.getString("surname"));
                teacher.setPatronymic(resultSet.getString("patronymic"));
                teachersData.add(teacher);
                System.out.println(teacher);
            }
        }
    }

    private void initStudentsData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM student");
            studentsData.clear();

            System.out.println();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setSurname(resultSet.getString("surname"));
                student.setPatronymic(resultSet.getString("patronymic"));
                student.setGroupId(resultSet.getInt("groupId"));
                student.setEmail(resultSet.getString("email"));
                student.setPhone(resultSet.getString("phone"));
                studentsData.add(student);
                System.out.println(student);
            }
        }
    }

    private void initTeachersSubjectsData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM teacher_subject");
            teachersSubjectsData.clear();

            System.out.println();
            while (resultSet.next()) {
                TeacherSubject teacherSubject = new TeacherSubject();
                teacherSubject.setId(resultSet.getInt("id"));
                teacherSubject.setSubjectId(resultSet.getInt("subjectId"));
                teacherSubject.setTeacherId(resultSet.getInt("teacherId"));
                teachersSubjectsData.add(teacherSubject);
                System.out.println(teacherSubject);
            }
        }
    }

    private void initLessonsData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM lesson");
            lessonsData.clear();

            System.out.println();
            while (resultSet.next()) {
                Lesson lesson = new Lesson();
                lesson.setId(resultSet.getInt("id"));
                lesson.setGroupId(resultSet.getInt("groupId"));
                lesson.setTeacher_subjectId(resultSet.getInt("teacher_subjectId"));
                lesson.setCabinet(resultSet.getInt("cabinet"));
                lesson.setDate(resultSet.getDate("date").toString());
                lesson.setTime(resultSet.getTime("time").toString());
                lessonsData.add(lesson);
                System.out.println(lesson);
            }
        }
    }

    private void addUser(String value) {
        User u = new User(value);
        try {
            String prepStat =
                    "INSERT INTO `flsdb`.`users` (`name`, `password`, `email`,`access_mode`) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, u.getUsername());
            preparedStatement.setString(2, u.getPassword());
            preparedStatement.setString(3, u.getEmail());
            preparedStatement.setInt(4, u.getAccessMode());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addLesson(String value) {
        Lesson u = new Lesson(value);
        try {
            String prepStat = "INSERT INTO `flsdb`.`lesson` (`groupId`, `teacher_subjectId`, `cabinet`, `date`, `time`) VALUES (?,?,?,?,?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, u.getGroupId());
            preparedStatement.setInt(2, u.getTeacher_subjectId());
            preparedStatement.setInt(3, u.getCabinet());
            preparedStatement.setDate(4, Date.valueOf(u.getDate()));
            preparedStatement.setTime(5, Time.valueOf(u.getTime()));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTeacherSubject(String value) {
        TeacherSubject u = new TeacherSubject(value);
        try {
            String prepStat = "INSERT INTO `flsdb`.`teacher_subject` (`teacherId`, `subjectId`) VALUES (?,?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, u.getTeacherId());
            preparedStatement.setInt(2, u.getSubjectId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addGroup(String value) {
        Group u = new Group(value);
        try {
            String prepStat =
                    "INSERT INTO `flsdb`.`group` (`Level`) VALUES (?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, u.getLevel());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addStudent(String value) {
        Student u = new Student(value);
        try {
            String prepStat = "INSERT INTO `flsdb`.`student` (`name`, `surname`, `patronymic`, `groupId`, `email`, `phone`) VALUES (?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, u.getName());
            preparedStatement.setString(2, u.getSurname());
            preparedStatement.setString(3, u.getPatronymic());
            preparedStatement.setInt(4, u.getGroupId());
            preparedStatement.setString(5, u.getEmail());
            preparedStatement.setString(6, u.getPhone());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTeacher(String value) {
        Teacher u = new Teacher(value);
        try {
            String prepStat = "INSERT INTO `flsdb`.`teacher` (`name`, `surname`, `patronymic`) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, u.getName());
            preparedStatement.setString(2, u.getSurname());
            preparedStatement.setString(3, u.getPatronymic());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSubject(String value) {
        Subject u = new Subject(value);
        try {
            String prepStat =
                    "INSERT INTO `flsdb`.`subject` (`name`, `hours`) VALUES (?,?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, u.getName());
            preparedStatement.setInt(2, u.getHours());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllUsers() {
        try {
            String prepStat = "DELETE FROM `flsdb`.`users` WHERE (`id` > -1)";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeAccountData(String id, String username, String password, String email) {
        try {
            if (email.equals("null"))
                email = null;
            String prepStat = "UPDATE `flsdb`.`users` SET `name` = ?, `password` = ?, `email` = ? WHERE (`id` = ?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, Integer.parseInt(id));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting...");
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainWindow.fxml"));
        primaryStage.setTitle("Main");
        Scene scene = new Scene(root, 800, 500, Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().add(new Image("assets/server-icon.png"));
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            primaryStage.setX(mouseEvent.getScreenX() - xOffset);
            primaryStage.setY(mouseEvent.getScreenY() - yOffset);
        });
        primaryStage.setMaximized(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        new Thread(this::listen).start();
    }

    @Override
    public void stop() throws Exception {
        serverSocket.close();
        super.stop();
    }

    public void updateLog() {
        connectionsTable.refresh();
        if (!textAreaLog.getText().equals(log)) {
            textAreaLog.setText(log);
            textAreaLog.setScrollTop(Double.MAX_VALUE);
        }
    }

    public void log(TCPConnection tcp, String value) {
        log += getCurrentDateTime() + " " + tcp + " " + tcp.getUsername() + ": " + value + "\n";
    }

    private void minimize() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
            minimizeButton.setStyle("-fx-background-image: url(assets/expand-white.png)");

        } else {
            stage.setMaximized(true);
            minimizeButton.setStyle("-fx-background-image: url(assets/minimize-white.png)");
        }
    }

    private String getCurrentDateTime() {
        ZonedDateTime zdt = ZonedDateTime.now();
        String year = String.valueOf(zdt.getYear());
        String month = String.valueOf(zdt.getMonthValue());
        String day = String.valueOf(zdt.getDayOfMonth());
        String hour = String.valueOf(zdt.getHour());
        String minute = String.valueOf(zdt.getMinute());
        String second = String.valueOf(zdt.getSecond());
        return year + "/" + month + "/" + day + "-" + hour + ":" + minute + ":" + second;
    }

    private void addDeleteButton() {
        TableColumn<TCPConnection, Void> deleteColumn = new TableColumn<>("");
        deleteColumn.setMinWidth(23);
        deleteColumn.setMaxWidth(23);
        deleteColumn.setResizable(false);


        Callback<TableColumn<TCPConnection, Void>, TableCell<TCPConnection, Void>> cellFactory4 = new Callback<>() {
            @Override
            public TableCell<TCPConnection, Void> call(TableColumn<TCPConnection, Void> param) {
                return new TableCell<>() {

                    private Button btn =
                            new Button("");

                    {
                        btn.getStyleClass().add("deleteClientButton");
                        btn.setMinWidth(15);
                        btn.setPrefWidth(15);
                        btn.setOnAction(event -> {
                            getTableView().getItems().get(getIndex()).disconnect();
                            connections.remove(getTableView().getItems().get(getIndex()));
                            connectionsTable.refresh();
                            new Thread(() -> System.out.println("disconnected")).start();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            if (currentLanguage.equals("English")) {
                                btn.setText("");
                            }
                            if (currentLanguage.equals("Russian")) {
                                btn.setText("");
                            }
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        deleteColumn.setCellFactory(cellFactory4);

        connectionsTable.getColumns().add(0, deleteColumn);
    }

    /*private boolean isFullnameUnique(String name, String surname, String patro) {
        for (Client c : clientsData)
            if (c.getName().equals(name.trim()) && c.getSurname().equals(surname.trim()) && c.getPatronymic().equals(patro.trim()))
                return false;
        return true;
    }
*/
    //TODO: ПРОДУБЛИРОВАТЬ ПРОВЕРКИ НА СЕРВЕР
}
