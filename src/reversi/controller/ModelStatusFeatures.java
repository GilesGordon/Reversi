package reversi.controller;


/**
 * This interface repersents requests a model can make. A Controller
 * could implement this interface.
 */
public interface ModelStatusFeatures {


  /**
   * When an exception is thrown, the model requests that the view display the excpetion.
   * @param s the message excpetion throw.
   */
  void requestDisplayMessage(String s);


  void requestDisplayBoard();
}
