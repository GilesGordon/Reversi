package reversi.view.graphic;

import java.awt.Point;

/**
 * Interface to provide functionality for our view decorators, specifcally our hint deceorator.
 */
public interface IDecorator {


  void displayHint(Point p, int score, boolean selected);


  void toggleHintsTurnedOn();

}
