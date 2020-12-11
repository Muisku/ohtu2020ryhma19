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
import java.util.logging.Level;
import java.util.logging.Logger;
import library.domain.ReadingTip;

/**
 * ReadingTipDatabaseDao Class. Used to access ReadingTips in the database.
 */
public class ReadingTipDatabaseDao implements ReadingTipDao {

    private String databaseAddress;

    public ReadingTipDatabaseDao(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }
    
    /**
     * Establishes database connection.
     * Executes PRAGMA statement needed to enable cascade on delete.
     * 
     * @return connection
     * @throws SQLException 
     */

    private Connection getConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(databaseAddress);

        Statement foreignKeysOn = conn.createStatement();
        foreignKeysOn.execute("PRAGMA foreign_keys = ON");
        createSchemaIfNotExists(conn);
        return conn;
    }
    
    /**
     * Queries the database for all reading tips.
     * 
     * @return list of reading tips
     */

    @Override
    public List<ReadingTip> getAllTips() {

        List<ReadingTip> readingTips = new ArrayList<>();

        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM ReadingTip");
            readingTips = createListFromResult(result);

            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return readingTips;
    }
    
    /**
     * Searches for reading tips with given search term and field.
     * 
     * @param searchTerm
     * @param searchField
     * @return 
     */

    @Override
    public List<ReadingTip> searchTip(String searchTerm, String searchField) {

        List<ReadingTip> readingTips = new ArrayList<>();
        try (Connection conn = getConnection()) {

            if (!searchField.equals("tags")) {

                String stmt = createStatementByField(searchField, searchTerm);
                PreparedStatement p = conn.prepareStatement(stmt);

                ResultSet result = p.executeQuery();
                readingTips = createListFromResult(result);

            } else if (searchField.equals("tags")) {
                int tagId = getTagId(searchTerm);
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM ReadingTip_Tag WHERE tag_id = ?");
                stmt.setInt(1, tagId);
                ResultSet result = stmt.executeQuery();
                readingTips = createListFromIds(result);
            }

            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return readingTips;
    }
    
    /**
     * Queries the database for a reading tip with the given id.
     * 
     * @param id
     * @return reading tip matching the id
     */

    @Override
    public ReadingTip getOneTip(String id) {

        List<ReadingTip> readingTips = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ReadingTip WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));

            ResultSet result = stmt.executeQuery();
            readingTips = createListFromResult(result);
            result.close();
            conn.close();
        } catch (SQLException se) {
            System.err.println("Getting one tip failed: " + se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (readingTips.size() == 1) {
            return readingTips.get(0);
        }
        return null;
    }
    
    private int getTagId(String tag) {

        List<Integer> tags = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tag WHERE name = ?");
            stmt.setString(1, tag);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                tags.add(result.getInt("id"));
            }

            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            System.err.println("Getting tag id failed:" + e.getMessage());
        }

        if (tags.size() == 1) {
            return tags.get(0);
        }
        return 0;
    }
    
    /**
     * Creates a database entry for the reading tip 
     * from the given ReadingTip object.
     * 
     * @param readingTip
     * @return true if creation succeeds, false if fails
     */

    @Override
    public boolean addTip(ReadingTip readingTip) {

        try (Connection conn = getConnection()) {

            createSchemaIfNotExists(conn);

            String[] tags = readingTip.getTags();
            int[] tagIds = new int[tags.length];

            for (int i = 0; i < tags.length; i++) {
                tagIds[i] = createTagIfNotExists(tags[i], conn);
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO ReadingTip (type, title, info1, info2) "
                    + "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, readingTip.getType());
            stmt.setString(2, readingTip.getTitle());
            stmt.setString(3, readingTip.getMoreInfo1());
            stmt.setString(4, readingTip.getMoreInfo2());
            stmt.execute();

            // retrieves the id of the most recent insert
            int readingTipId = (int) stmt.getGeneratedKeys().getLong(1);

            linkTagsWithReadingTip(tagIds, readingTipId, conn);

            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Searches the Tag table for a given tag name. If a tag exists, retrieve
     * its id. If a tag is not found, add a new entry to the table and return
     * its id.
     *
     * @param tag name of the tag
     * @param conn connection to database
     * @return tag id
     */
    private int createTagIfNotExists(String tag, Connection conn) throws SQLException {

        int id = -1;
        
        //checks for tag name in the tag table
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tag WHERE name = ?");
        stmt.setString(1, tag);
        ResultSet result = stmt.executeQuery();

        if (!result.next()) {  // if tag not found, create tag and get id
            stmt = conn.prepareStatement("INSERT INTO Tag (name) "
                    + "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, tag);
            stmt.execute();

            // retrieves the id of the most recent insert
            id = (int) stmt.getGeneratedKeys().getLong(1);

        } else {
            id = result.getInt("id");
        }

        return id;
    }

    /**
     * Links the IDs of the reading tip and its tags by creating entries to the
     * composite table ReadingTip_Tag.
     *
     * @param tagIds ids of the tags
     * @param readingTipId id of the ReadingTip
     * @param conn connection to database
     * @throws SQLException exception
     */
    private void linkTagsWithReadingTip(
            int[] tagIds, int readingTipId, Connection conn) throws SQLException {
        for (int tagId : tagIds) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO ReadingTip_Tag (readingtip_id, tag_id) "
                    + "VALUES (?,?)");
            stmt.setInt(1, readingTipId);
            stmt.setInt(2, tagId);
            stmt.execute();
        }
    }
    
    /**
     * Removes all tags associated with a given reading tip id
     * 
     * @param readingTipId
     * @param conn
     * @throws SQLException 
     */

    private void removeAllTags(int readingTipId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM ReadingTip_Tag "
                + "WHERE readingtip_id = ?");
        stmt.setInt(1, readingTipId);
        stmt.executeUpdate();
    }
    
    /**
     * Removes database entry matching the given id
     * 
     * @param id
     * @return true if deletion succeeds, false if fails
     */

    @Override
    public boolean removeTip(String id) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM ReadingTip WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }

        return true;
    }
    
    /**
     * Updates an existing database entry if new values were given
     * 
     * @param id
     * @param newTitle
     * @param newInfo1
     * @param newInfo2
     * @return true if update succeeds, false if fails
     */

    @Override
    public boolean modifyTip(String id, String newTitle, String newInfo1, String newInfo2) {

        try (Connection conn = getConnection()) {
            if (!newTitle.isEmpty()) {
                PreparedStatement stmt
                        = conn.prepareStatement("UPDATE ReadingTip SET title = ? WHERE id = ?");
                stmt.setString(1, newTitle);
                stmt.setInt(2, Integer.parseInt(id));
                stmt.executeUpdate();
            }

            if (!newInfo1.isEmpty()) {
                PreparedStatement stmt
                        = conn.prepareStatement("UPDATE ReadingTip SET info1 = ? WHERE id = ?");
                stmt.setString(1, newInfo1);
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

            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
            return false;

        }

        return true;

    }
    
    /**
     * Updates reading tip-tag associations
     * 
     * @param readingTipId
     * @param newTags
     * @param replace
     * @return true if update succeeds, false if fails
     */

    @Override
    public boolean modifyTags(String readingTipId, String[] newTags, boolean replace) {

        try (Connection conn = getConnection()) {

            createSchemaIfNotExists(conn);

            int[] tagIds = new int[newTags.length];

            for (int i = 0; i < newTags.length; i++) {
                tagIds[i] = createTagIfNotExists(newTags[i], conn);
            }

            if (replace) {
                removeAllTags(Integer.parseInt(readingTipId), conn);
            }
            linkTagsWithReadingTip(tagIds, Integer.parseInt(readingTipId), conn);

            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Creates database tables if it doesn't exist.
     */
    private void createSchemaIfNotExists(Connection conn) throws SQLException {

        Statement stmt = conn.createStatement();

        stmt.execute(
                "CREATE TABLE IF NOT EXISTS ReadingTip ("
                + "id INTEGER PRIMARY KEY, "
                + "type TEXT, "
                + "title TEXT, "
                + "info1 TEXT, "
                + "info2 TEXT, "
                + "read INTEGER DEFAULT 0)"); //0=not read, 1=read

        stmt.execute(
                "CREATE TABLE IF NOT EXISTS Tag ("
                + "id INTEGER PRIMARY KEY, "
                + "name TEXT)");

        stmt.execute(
                "CREATE TABLE IF NOT EXISTS ReadingTip_Tag ("
                + "readingtip_id INTEGER NOT NULL REFERENCES ReadingTip(id) ON DELETE CASCADE, "
                + "tag_id INTEGER NOT NULL REFERENCES Tag(id) ON DELETE CASCADE, "
                + "PRIMARY KEY (readingtip_id, tag_id))"
        );

    }
    
    /**
     * Changes the read value of given reading tip to 1
     * 
     * @param id
     * @return true if succeeds, false if fails
     */

    @Override
    public boolean markAsRead(String id) {

        try (Connection conn = getConnection()) {

            PreparedStatement stmt
                    = conn.prepareStatement("UPDATE ReadingTip SET read = 1 WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }

        return true;
    }
    
    /**
     * Changes the read value of given reading tip to 0
     * 
     * @param id
     * @return true if succeeds, false if fails
     */

    @Override
    public boolean markAsUnread(String id) {

        try (Connection conn = getConnection()) {
            PreparedStatement stmt
                    = conn.prepareStatement("UPDATE ReadingTip SET read = 0 WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }

        return true;
    }
    
    /**
     * Deletes the database file
     * 
     */

    @Override
    public void deleteDatabaseContents() {
        int location = databaseAddress.lastIndexOf(":");
        String fileName = databaseAddress.substring(location + 1);
        File file = new File(fileName);
        file.delete();
    }
    
    /**
     * Builds a different query string based on search field and term.
     * 
     * @param searchField
     * @param searchTerm
     * @return built string
     */

    private String createStatementByField(String searchField, String searchTerm) {
        StringBuilder stmt = new StringBuilder();

        stmt.append("SELECT * FROM ReadingTip where ");

        if (!searchField.isEmpty()) {
            stmt.append(searchField + " LIKE '%" + searchTerm + "%'");
        } else {
            stmt.append("type LIKE '%" + searchTerm + "%' OR ");
            stmt.append("title LIKE '%" + searchTerm + "%' OR ");
            stmt.append("info1 LIKE '%" + searchTerm + "%' OR ");
            stmt.append("info2 LIKE '%" + searchTerm + "%'");
        }

        return stmt.toString();
    }
    
    /**
     * Transforms database query results into a list of ReadingTip objects
     * 
     * @param result
     * @return results as a list of ReadingTip Objects
     * @throws Exception 
     */

    private List<ReadingTip> createListFromResult(ResultSet result) throws Exception {

        List<ReadingTip> readingTips = new ArrayList<>();

        while (result.next()) {
            int id = result.getInt("id");
            String type = result.getString("type");
            String title = result.getString("title");
            String info1 = result.getString("info1");
            String info2 = result.getString("info2");
            int read = result.getInt("read");

            ReadingTip readingtip = new ReadingTip(title, type);
            readingtip.setId(id);
            readingtip.setMoreInfo1(info1);
            readingtip.setMoreInfo2(info2);
            readingtip.setRead(read);
            readingtip.setTags(fetchTagsForReadingTip(id));
            readingTips.add(readingtip);
        }
        return readingTips;
    }
    
    /**
     * Fetches all the tags associated with the given reading tip id
     * 
     * @param id
     * @return tags in a String array
     */

    private String[] fetchTagsForReadingTip(int id) {

        try (Connection conn = getConnection()) {

            PreparedStatement stmt
                    = conn.prepareStatement("SELECT name FROM ReadingTip_Tag JOIN"
                            + " Tag ON tag_id = id WHERE readingtip_id = ?");
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();

            ArrayList<String> tags = new ArrayList<>();
            while (result.next()) {
                tags.add(result.getString(1));
            }
            result.close();
            stmt.close();
            conn.close();
            String[] retval = new String[tags.size()];
            return tags.toArray(retval);

        } catch (SQLException se) {
            System.err.println("Fetching tags failed: " + se.getMessage());
        }

        return new String[0];
    }

    private List<ReadingTip> createListFromIds(ResultSet result) throws Exception {

        List<ReadingTip> readingTips = new ArrayList<>();

        while (result.next()) {
            int id = result.getInt("readingtip_id");
            ReadingTip readingtip = getOneTip(Integer.toString(id));
            readingTips.add(readingtip);
        }

        return readingTips;
    }
}
