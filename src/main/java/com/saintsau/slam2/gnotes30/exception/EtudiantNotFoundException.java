package com.saintsau.slam2.gnotes30.exception;

public class EtudiantNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -9136014779678006557L;

    public EtudiantNotFoundException(Long id) {
        super("Étudiant avec l'ID " + id + " introuvable.");
    }
}
