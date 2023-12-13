package reversi.provider.player.strategies;

import java.util.ArrayList;

import reversi.provider.model.HexaCoordinate;
import reversi.provider.model.ReversiModelReadOnly;

/**
 * A reversi.player.aiStrategies.ReversiStrategies is the interface which all strategies for the
 * AI will be based off of. These strategies are always assumed to be fallible. All the
 * strategies assume that they are being given valid moves, as that should be handled in the
 * player object. It also assumes that the strategy will be able to return something.
 */
public interface ReversiStrategies {
  /**
   * Choose move will implement the strategy we want for any strategy class (a class that
   * implements ReversiStrategies). The strategy will detect the most optimal coordinates to
   * place a tile and will return them as a list.
   * The lists are ordered in such a way that the earlier in the list a coordinate is, the more
   * "top left" on the board it is.
   * By "top left", we mean that it is the highest row (most negative R value), and amongst all the
   * coordinates in that row, ti is the leftmost (most negative Q value).
   * Thus, the first element in the list is the top leftmost coordinate and the last element is the
   * bottom rightmost element.
   *
   * @param model          the game which is being played on, read only so there is no accidental
   *                       mutation.
   * @param possibleCoords a list of valid coordinates that could be moved to.
   * @return All viable coordinates to move to, sorted from top leftmost to the bottom rightmost.
   */
  ArrayList<HexaCoordinate> chooseMove(ReversiModelReadOnly model,
                                       ArrayList<HexaCoordinate> possibleCoords);
}
