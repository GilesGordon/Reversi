package reversi.controller;

import reversi.model.Coordinate;

/**
 * Repersents the potential actions a player could make.
 */
public interface PlayerActions {

  /**
   * Requests that {@code doMove} be done.
   */
  void requestDoMove(Coordinate c);

  /**
   * Requests that {@code passTurn} be done.
   */
  void requestPass();



}

