package de.craftq.tim;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.craftq.tim.utils.VaultHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CEconomy extends JavaPlugin {

    private static CEconomy plugin;

    private static HikariDataSource moneyDataSource;

    File configFile = new File(getDataFolder(), "mysql.yml");

    public static String pr = translateHexColorCodes("&#54F465M&#55F574i&#56F683n&#57F792e&#58F8A1T&#59F9AFr&#5AFABEa&#5BFBCDd&#5CFCDCe §8| §7");
    public static String noperm = pr + "§cDazu hast Du keine Rechte.";
    public static String noplayer = pr + "§cNur für Spieler";

    public static String translateHexColorCodes(String message) {
        return message.replaceAll("&#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])", "§x§$1§$2§$3§$4§$5§$6");
    }

    @Override
    public void onEnable() {

        loadMySQL();

        plugin = this;

        System.out.println("[MineQEconomy] Das Plugin wurde aktiviert.");

        if(getServer().getPluginManager().getPlugin("Vault") != null) {
            getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultHandler(this),
                    this, ServicePriority.Normal);
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[MineQEconomy] Das Plugin wurde deaktiviert.");

        if(moneyDataSource != null) moneyDataSource.close();
    }

    public static CEconomy getPlugin() {
        return plugin;
    }

    public void loadMySQL() {

        // Falls die Datei nicht existiert, erstelle sie mit Standardwerten
        if (!configFile.exists()) {
            saveDefaultMySQLConfig(configFile);
        }

        // Lade die Konfigurationsdatei
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // MySQL-Daten aus der Config laden
        String host = config.getString("mysql.host", "127.0.0.1");
        int port = config.getInt("mysql.port", 3306);
        String database = config.getString("mysql.database", "Economy");
        String username = config.getString("mysql.username", "root");
        String password = config.getString("mysql.password", "password");
        int maxPoolSize = config.getInt("mysql.maxPoolSize", 10); // Standardwert 10 falls nicht gesetzt

        // HikariCP-Config setzen
        HikariConfig moneyConfig = new HikariConfig();
        moneyConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        moneyConfig.setUsername(username);
        moneyConfig.setPassword(password);
        moneyConfig.setMaximumPoolSize(maxPoolSize);

        // Datenquelle starten
        moneyDataSource = new HikariDataSource(moneyConfig);
    }

    public static Connection getMoneyConnection() throws SQLException {
        return moneyDataSource.getConnection();
    }

    private void saveDefaultMySQLConfig(File configFile) {
        FileConfiguration config = new YamlConfiguration();

        // Standardwerte für MySQL-Verbindung
        config.set("mysql.host", "127.0.0.1");
        config.set("mysql.port", 3306);
        config.set("mysql.database", "Economy");
        config.set("mysql.username", "root");
        config.set("mysql.password", "password");
        config.set("mysql.maxPoolSize", 10);

        try {
            config.save(configFile);
            System.out.println("[MineTrade-Economy] mysql.yml wurde erstellt!");
        } catch (IOException e) {
            System.out.println("[MineTrade-Economy] Fehler beim Erstellen der mysql.yml!");
            e.printStackTrace();
        }
    }

}