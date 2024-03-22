public class Resume {
    public String gagnant;
    public String mainCroupier;
    public String scoreCroupier;
    public String mainJoueur;
    public String scoreJoueur;

    public Resume(String _gagnant, String _mainCroupier, String _scoreCroupier, String _mainJoueur, String _scoreJoueur) {
        this.gagnant = _gagnant;
        this.mainCroupier = _mainCroupier;
        this.scoreCroupier = _scoreCroupier;
        this.mainJoueur = _mainJoueur;
        this.scoreJoueur = _scoreJoueur;
    }

    public Resume(String _gagnant, String _mainJoueur, String _scoreJoueur) {
        this.gagnant = _gagnant;
        this.mainJoueur = _mainJoueur;
        this.scoreJoueur = _scoreJoueur;
    }
}
