import java.sql.*;
import java.util.*;

public class SkillAnalyzer {

    public static void analyze(String targetRole, String studentSkills) {

        String sql = "SELECT required_skills FROM job_roles " +
                     "WHERE LOWER(role_name) = LOWER(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, targetRole);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println();
                System.out.println("Role not found in database.");
                System.out.println("Please choose a role available in job_roles table.");
                return;
            }

            String requiredSkills = rs.getString("required_skills");

            Set<String> studentSet = convertToSet(studentSkills);
            Set<String> requiredSet = convertToSet(requiredSkills);

            Set<String> matched = new LinkedHashSet<>(studentSet);
            matched.retainAll(requiredSet);

            Set<String> missing = new LinkedHashSet<>(requiredSet);
            missing.removeAll(studentSet);

            double percentage = 0;

            if (!requiredSet.isEmpty()) {
                percentage = (matched.size() * 100.0) / requiredSet.size();
            }

            System.out.println();
            System.out.println("====================================");
            System.out.println("        SKILL GAP ANALYSIS");
            System.out.println("====================================");

            System.out.println("Target Role: " + targetRole);

            System.out.println();
            System.out.println("Required Skills:");
            for (String skill : requiredSet) {
                System.out.println("  - " + skill);
            }

            System.out.println();
            System.out.println("Skills You Have:");
            if (matched.isEmpty()) {
                System.out.println("  None");
            } else {
                for (String skill : matched) {
                    System.out.println("  ✓ " + skill);
                }
            }

            System.out.println();
            System.out.println("Missing Skills:");
            if (missing.isEmpty()) {
                System.out.println("  None 🎉");
            } else {
                for (String skill : missing) {
                    System.out.println("  ✗ " + skill);
                }
            }

            System.out.println();
            System.out.printf("Skill Match: %.2f%%%n", percentage);

            System.out.println();
            System.out.println("Recommendation:");

            if (missing.isEmpty()) {
                System.out.println("  Excellent! You have all required skills.");
            } else {
                System.out.println("  Focus on learning:");
                for (String skill : missing) {
                    System.out.println("  → " + skill);
                }
            }

            System.out.println("====================================");

        } catch (SQLException e) {
            System.out.println("Skill analysis failed.");
            e.printStackTrace();
        }
    }

    private static Set<String> convertToSet(String skills) {

        Set<String> result = new LinkedHashSet<>();

        String[] skillArray = skills.split(",");

        for (String skill : skillArray) {

            String cleaned = skill.trim().toLowerCase();

            if (!cleaned.isEmpty()) {
                result.add(cleaned);
            }
        }

        return result;
    }
}