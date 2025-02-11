package com.tca.DAO;

import com.tca.Models.Dono;
import com.tca.Models.Pet;
import com.tca.db.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    public void inserirPet(Pet pet) throws SQLException {
        String petInsertSql = "INSERT INTO Pet (pet_nome, pet_raca, pet_tipo, pet_idade, pet_sexo, dono_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(petInsertSql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getRaca());
            stmt.setString(3, pet.getTipo());
            stmt.setInt(4, pet.getIdade());
            stmt.setString(5, pet.getSexo());
            stmt.setInt(6, pet.getDono().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        pet.setId(generatedId);
                    }
                }
            }
        }
    }
    
    public void atualizarPet(Pet pet) throws SQLException {
        String sql = "UPDATE Pet SET pet_nome = ?, pet_raca = ?, pet_tipo = ?, pet_idade = ?, pet_sexo = ? WHERE pet_id = ?";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getRaca());
            stmt.setString(3, pet.getTipo());
            stmt.setInt(4, pet.getIdade());
            stmt.setString(5, pet.getSexo());
            stmt.setInt(6, pet.getId());
    
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pet atualizado com sucesso!");
            } else {
                System.err.println("Nenhum pet foi atualizado. Verifique o ID.");
            }
        }
    }
    
    public Pet buscarPetPorId(int id) throws SQLException {
    String sql = "SELECT p.*, d.dono_id, d.dono_nome, d.dono_telefone, d.dono_email " +
                 "FROM Pet p INNER JOIN Dono d ON p.dono_id = d.dono_id " +
                 "WHERE p.pet_id = ?";

    try (Connection conn = DatabaseConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Dono dono = new Dono(
                    rs.getInt("dono_id"),
                    rs.getString("dono_nome"),
                    rs.getString("dono_telefone"),
                    rs.getString("dono_email")
                );

                Pet pet = new Pet(
                    rs.getInt("pet_id"),
                    rs.getString("pet_nome"),
                    rs.getString("pet_raca"),
                    rs.getString("pet_tipo"),
                    rs.getInt("pet_idade"),
                    rs.getString("pet_sexo"),
                    dono
                );

                return pet;
            }
        }
    }
    return null;
}

    

    public List<Pet> buscarPetsPorDonoId(int donoId) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM Pet WHERE dono_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, donoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet pet = new Pet(
                            rs.getInt("pet_id"),
                            rs.getString("pet_nome"),
                            rs.getString("pet_raca"),
                            rs.getString("pet_tipo"),
                            rs.getInt("pet_idade"),
                            rs.getString("pet_sexo"),
                            null
                    );
                    pets.add(pet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pets;
    }

    public void excluirPet(int petId) {
        String sql = "DELETE FROM Pet WHERE pet_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, petId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pet excluído com sucesso.");
            } else {
                System.out.println("Nenhum pet encontrado com o ID: " + petId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pet buscarPetPorNomeEDono(String nomePet, int donoId) throws SQLException {
        String sql = "SELECT * FROM Pet WHERE pet_nome = ? AND dono_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, nomePet);
            stmt.setInt(2, donoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getInt("pet_id"));
                    pet.setNome(rs.getString("pet_nome"));
                    pet.setRaca(rs.getString("pet_raca"));
                    pet.setTipo(rs.getString("pet_tipo"));
                    pet.setIdade(rs.getInt("pet_idade"));
                    pet.setSexo(rs.getString("pet_sexo"));
                    return pet;
                }
            }
        }
        return null;
    }
    
    public List<Pet> buscarTodosPets() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT p.*, d.dono_id, d.dono_nome, d.dono_telefone, d.dono_email " +
                     "FROM Pet p INNER JOIN Dono d ON p.dono_id = d.dono_id";
    
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                Dono dono = new Dono(
                    rs.getInt("dono_id"),
                    rs.getString("dono_nome"),
                    rs.getString("dono_telefone"),
                    rs.getString("dono_email")
                );
    
                Pet pet = new Pet(
                    rs.getInt("pet_id"),
                    rs.getString("pet_nome"),
                    rs.getString("pet_raca"),
                    rs.getString("pet_tipo"),
                    rs.getInt("pet_idade"),
                    rs.getString("pet_sexo"),
                    dono
                );
    
                pets.add(pet);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return pets;
    }
      
}
