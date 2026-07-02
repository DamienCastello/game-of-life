package gameoflife.dto;

public class StartRequest {
    public String pattern;

    // Utilisée uniquement lorsque pattern == "custom"
    public boolean[][] grid;
}