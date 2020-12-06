package library.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import library.domain.ReadingTip;


/**
 * ReadingTipDatabaseDao Class. Used to access ReadingTips in the database.
 */
public class ReadingTipDatabaseDao implements ReadingTipDao {

    private String databaseAddress;

    public ReadingTipDatabaseDao(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }

    @Override
    public List<ReadingTip> getAllTips() throws Exception {

        Connection conn = DriverManager.getConnection(databaseAddress);
        List<ReadingTip> readingTips = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM ReadingTip");
            readingTips = createListFromResult(result);

        } catch (Exception e) {
            System.out.println("Database is empty.");
        }

        conn.close();

        return readingTips;

    }

    @Override
    public List<ReadingTip> searchTip(String searchTerm, String searchField) throws Exception {
        Connection conn = DriverManager.getConnection(databaseAddress);
        List<ReadingTip> readingTips = new ArrayList<>();
        try {
            String stmt = createStatementByField(searchField, searchTerm);
            PreparedStatement p = conn.prepareStatement(stmt);

            ResultSet result = p.executeQuery();
            readingTips = createListFromResult(result);
        } catch (Exception e) {

        }
        conn.close();
        return readingTips;
    }

    @Override
    public ReadingTip getOneTip(String id) throws Exception {
        Connection conn = DriverManager.getConnection(databaseAddress);
        List<ReadingTip> readingTips = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ReadingTip WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));

            ResultSet result = stmt.executeQuery();
            readingTips = createListFromResult(result);
        } catch (Exception e) {
        }

        conn.close();

        if (readingTips.size() == 1) {
            return readingTips.get(0);
        }
        return null;
    }

    @Override
    public void addTip(ReadingTip readingTip) throws Exception {

        Connection conn = DriverManager.getConnection(databaseAddress);
        createSchemaIfNotExists(conn);

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO ReadingTip (type, title, author, info2) "
                + "VALUES (?,?,?,?)");

        stmt.setString(1, readingTip.getType());
        stmt.setString(2, readingTip.getTitle());
        stmt.setString(3, readingTip.getAuthor());
        stmt.setString(4, readingTip.getMoreInfo2());
        stmt.execute();

        conn.close();
    }

    @Override
    public void removeTip(String id) throws Exception {

        Connection conn = DriverManager.getConnection(databaseAddress);
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM ReadingTip WHERE id = ?");
        stmt.setInt(1, Integer.parseInt(id));
        stmt.executeUpdate();
        conn.close();
    }

    @Override
    public void modifyTip(String id, String newTitle, String newAuthor, String newInfo2)
            throws Exception {
        Connection conn = DriverManager.getConnection(databaseAddress);

        try {
            if (!newTitle.isEmpty()) {
                PreparedStatement stmt
                        = conn.prepareStatement("UPDATE ReadingTip SET title = ? WHERE id = ?");
                stmt.setString(1, newTitle);
                stmt.setInt(2, Integer.parseInt(id));
                stmt.executeUpdate();
            }

            if (!newAuthor.isEmpty()) {
                PreparedStatement stmt
                        = conn.prepareStatement("UPDATE ReadingTip SET author = ? WHERE id = ?");
                stmt.setString(1, newAuthor);
                stmt.setInt(2, Integer.parseInt(id));
                stmt.executeUpdate();
            }

            if (!newInfo2.isEmpty()) {
                PreparedStatement stmt
                        = conn.prepareStatement("UPDATE ReadingTip SET info2 = ? WHERE id = ?");
                stmt.setString(1, newInfo2);
                stmt.setInt(2, Integer.parseInt(id));
                stmt.executeUpdate();
            }

        } catch (Exception e) {
        }
        conn.close();
    }

    /**
     * Creates ReadingTip table if it doesn't exist.
     */
    public void createSchemaIfNotExists(Connection conn) throws SQLException {

        Statement stmt = conn.createStatement();

        try {

            stmt.execute(
                    "CREATE TABLE ReadingTip ("
                    + "id INTEGER PRIMARY KEY, "
                    + "type TEXT, "
                    + "title TEXT, "
                    + "author TEXT, "
                    + "info2 TEXT, "
                    + "read INTEGER DEFAULT 0)"); //0=not read, 1=read
        } catch (Exception e) {
            System.out.println("Database schema already exists.");
        }

    }

    @Override
    public void markAsRead(String id) {
        try {
            Connection conn = DriverManager.getConnection(databaseAddress);
            PreparedStatement stmt
                    = conn.prepareStatement("UPDATE ReadingTip SET read = 1 WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println("exception: " + e);
        }
    }

    @Override
    public void markAsUnread(String id) {
        try {
            Connection conn = DriverManager.getConnection(databaseAddress);
            PreparedStatement stmt
                    = conn.prepareStatement("UPDATE ReadingTip SET read = 0 WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
            conn.close();
        } catch (Exception e) {
        }
    }

    @Override
    public void deleteDatabaseContents() {
        int location = databaseAddress.lastIndexOf(":");
        String fileName = databaseAddress.substring(location + 1);
        File file = new File(fileName);
        file.delete();
        //System.out.println("going to delete: " + file.getAbsolutePath());
    }

    private String createStatementByField(String searchField, String searchTerm) {
        StringBuilder stmt = new StringBuilder();

        stmt.append("SELECT * FROM ReadingTip where ");

        if (!searchField.isEmpty()) {
            stmt.append(searchField + " LIKE '%" + searchTerm + "%'");
        } else {
            stmt.append("type LIKE '%" + searchTerm + "%' OR ");
            stmt.append("title LIKE '%" + searchTerm + "%' OR ");
            stmt.append("author LIKE '%" + searchTerm + "%' OR ");
            stmt.append("info2 LIKE '%" + searchTerm + "%'");
        }

        return stmt.toString();
    }

    private List<ReadingTip> createListFromResult(ResultSet result) throws Exception {
        List<ReadingTip> readingTips = new ArrayList<>();
        while (result.next()) {
            int id = result.getInt("id");
            String type = result.getString("type");
            String title = result.getString("title");
            String author = result.getString("author");
            String info2 = result.getString("info2");
            int read = result.getInt("read");

            ReadingTip readingtip = new ReadingTip(title, type);
            readingtip.setId(id);
            readingtip.setAuthor(author);
            readingtip.setMoreInfo2(info2);
            readingtip.setRead(read);
            readingTips.add(readingtip);
        }
        return readingTips;
    }

}
