package reversi.controller;

import reversi.model.Coordinate;

/**
 * Our features interface is a list of Feature commands that can be used by implementing classes.
 *
 */
public interface Features {


  /**
   * Requests that {@code doMove} be done.
   */
  void requestDoMove(Coordinate c);

  /**
   * Requests that {@code passTurn} be done.
   */

  void requestPass();




}
