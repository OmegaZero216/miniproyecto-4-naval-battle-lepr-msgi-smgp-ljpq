package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception;

/**
 * Unchecked: thrown when LoadManager.load() is called but no save file
 * exists. Treated as a programming error at the call site — callers
 * (e.g. TitleController) are expected to check SaveManager.saveExists()
 * before offering "Cargar Partida" as an option at all.
 */
public class SaveNotFoundException extends RuntimeException {
  public SaveNotFoundException(String message) {
    super(message);
  }
}
