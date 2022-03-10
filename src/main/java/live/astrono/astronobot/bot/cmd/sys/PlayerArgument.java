package main.java.live.astrono.astronobot.bot.cmd.sys;

import main.java.live.astrono.astronobot.bot.ingame.Rank;
import main.java.live.astrono.astronobot.bot.ingame.RankBranch;
import main.java.live.astrono.astronobot.database.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerArgument {
    private String name;
    private HashMap<String, Object> data;

    public PlayerArgument(String name) {
        this.name = name;

        HashMap<String, Object> resultSet = new HashMap<>();

        try {
            PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(
                    "SELECT * FROM players WHERE username = '" + name + "' LIMIT 1");
            statement.setQueryTimeout(10);

            if (statement.execute()) {
                ResultSet set = statement.getResultSet();

                while (set.next()) {
                    ResultSetMetaData metaData = set.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        Object object = set.getObject(i);
                        resultSet.put(metaData.getColumnName(i), object);

                        // Parse lists in the database
                        ArrayList<String> stringList = new ArrayList<>();
                        if (object instanceof String) {
                            String s = (String) object;
                            if (s.equals("[]")) {
                                resultSet.put(metaData.getColumnName(i), stringList);
                                continue;
                            }

                            if (s.startsWith("[")) {
                                if (s.endsWith("]")) {
                                    s = s.replace("[", "").replace("]", "");
                                    List<String> list = List.of(s.split(","));
                                    for (String str : list) {
                                        stringList.add(str);
                                    }

                                    resultSet.put(metaData.getColumnName(i), stringList);
                                }
                            }
                        }
                    }
                }
                set.close();
            } else {
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.data = resultSet;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public boolean exists() {
        return (!(data.get("username") == null));
    }

    public String getHead() {
        return "https://crafatar.com/renders/head/" + data.get("uuid");
    }

    public String getFace() {
        return "https://crafatar.com/avatars/" + data.get("uuid");
    }


    public List<Rank> getRanks() {
        ArrayList<Rank> ranks = new ArrayList<>();


        for (RankBranch rankBranch : RankBranch.values()) {
            int value = (int) data.get(rankBranch.name().toLowerCase());

            for (Rank r : Rank.values()) {
                if (r.rankBranch == rankBranch) {
                    if (r.weight == value) {
                        if (r.weight == 0)
                            continue;
                        ranks.add(r);
                    }
                }
            }
        }

        return ranks;
    }
}
