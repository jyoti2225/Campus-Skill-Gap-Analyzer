import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("     CAMPUS SKILL GAP ANALYZER");
        System.out.println("====================================");

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        System.out.print("Enter course: ");
        String course = sc.nextLine();

        System.out.print("Enter your skills (comma separated): ");
        String skills = sc.nextLine();

        // Show available job roles
        System.out.println();
        System.out.println("Available Job Roles:");
        System.out.println("------------------------------------");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT id, role_name FROM job_roles ORDER BY id";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + ". " +
                    rs.getString("role_name")
                );
            }

            System.out.println("------------------------------------");

        } catch (Exception e) {
            System.out.println("Unable to load job roles.");
            e.printStackTrace();
            sc.close();
            return;
        }

        System.out.print("Choose target job role number: ");
        int roleId = sc.nextInt();

        String targetRole = "";

        // Get selected role name
        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT role_name FROM job_roles WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, roleId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                targetRole = rs.getString("role_name");
            } else {
                System.out.println("Invalid role number.");
                sc.close();
                return;
            }

        } catch (Exception e) {
            System.out.println("Unable to find selected role.");
            e.printStackTrace();
            sc.close();
            return;
        }

        // Save student
        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO students " +
                         "(name, course, skills, target_role) " +
                         "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, course);
            ps.setString(3, skills);
            ps.setString(4, targetRole);

            ps.executeUpdate();

            System.out.println();
            System.out.println("Student added successfully!");

        } catch (Exception e) {
            System.out.println("Unable to save student.");
            e.printStackTrace();
            sc.close();
            return;
        }

// Skill Gap Analysis
SkillAnalyzer.analyze(
    targetRole,
    skills
);

        sc.close();
    }
}