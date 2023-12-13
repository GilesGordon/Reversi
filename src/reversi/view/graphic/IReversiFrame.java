package reversi.view.graphic;





import reversi.controller.Features;


/**
 * Represents a view Interface for our Game.
 */
public interface IReversiFrame {


  /**
   * Regenerates the view.
   */
  public void rePaint();


  /**
   * Adds features to a ReversiFrame.
   * @param features the Feature (or a feature implementation aka a Controller) that is being added.
   */
  public void addFeatures(Features features);

  /**
   * Displays to user that a message.
   */
  public void displayNotification(String s);

  /**
   * Assigns the view a state representing their player.
   */
  public void addState(String state);

  void setVisible(boolean b);

  public void displayDoMove();


}
