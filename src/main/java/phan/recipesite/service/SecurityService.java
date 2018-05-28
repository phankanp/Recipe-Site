package phan.recipesite.service;

public interface SecurityService {
    boolean login(String username, String password);

    String findLoggedInUsername();
}