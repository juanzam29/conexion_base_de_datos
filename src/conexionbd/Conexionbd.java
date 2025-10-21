/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package conexionbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
/**
 *
 * @author Diego
 */



public class Conexionbd {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection con = null;

        try {
            // 1. Cargar el driver JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 2. Conectar a la base de datos
            String connectionURL = "jdbc:sqlserver://DESKTOP-5K1DA26\\SQLEXPRESS:1433;databaseName=Mercado;user=usuarioSQL;password=123;encrypt=true;trustServerCertificate=true;";
            con = DriverManager.getConnection(connectionURL);
            System.out.println("✅ Conexión exitosa a la base de datos");

            boolean salir = false;

            while (!salir) {
                System.out.println("\n--- MENÚ DE PRODUCTOS ---");
                System.out.println("1. Listar productos");
                System.out.println("2. Agregar producto");
                System.out.println("3. Eliminar producto");
                System.out.println("4. Salir");
                System.out.print("Elige una opción: ");
                int opcion = sc.nextInt();
                sc.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        listarProductos(con);
                        break;
                    case 2:
                        agregarProducto(con, sc);
                        break;
                    case 3:
                        eliminarProducto(con, sc);
                        break;
                    case 4:
                        salir = true;
                        System.out.println("👋 Programa finalizado.");
                        break;
                    default:
                        System.out.println("⚠️ Opción no válida.");
                }
            }

        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: No se encontró el driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión o ejecución SQL.");
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 📜 Listar productos
    private static void listarProductos(Connection con) throws SQLException {
        Statement st = con.createStatement();
        // 👇 Nombre corregido de la tabla
        ResultSet rs = st.executeQuery("SELECT * FROM dbo.Producto");

        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        while (rs.next()) {
            int id = rs.getInt("ID");
            String nombre = rs.getString("Nombre");
            int precio = rs.getInt("Precio");
            System.out.println(id + " | " + nombre + " | $" + precio);
        }
    }

    // 🆕 Agregar producto
    private static void agregarProducto(Connection con, Scanner sc) throws SQLException {
        System.out.print("Nombre del producto: ");
        String nombre = sc.nextLine();
        System.out.print("Precio del producto: ");
        int precio = sc.nextInt();
        sc.nextLine();

        // 👇 Nombre corregido de la tabla
        String sql = "INSERT INTO dbo.Producto (Nombre, Precio) VALUES ('" + nombre + "', " + precio + ")";
        Statement st = con.createStatement();
        st.execute(sql);
        System.out.println("✅ Producto agregado con éxito.");
    }

    // 🗑️ Eliminar producto
    private static void eliminarProducto(Connection con, Scanner sc) throws SQLException {
        System.out.print("ID del producto a eliminar: ");
        int id = sc.nextInt();
        sc.nextLine();

        // 👇 Nombre corregido de la tabla
        String sql = "DELETE FROM dbo.Producto WHERE ID = " + id;
        Statement st = con.createStatement();
        int filas = st.executeUpdate(sql);

        if (filas > 0) {
            System.out.println("✅ Producto eliminado correctamente.");
        } else {
            System.out.println("⚠️ No se encontró ningún producto con ese ID.");
        }
    }
}
