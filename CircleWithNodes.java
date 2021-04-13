package CircleWithNodes;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



    







public class CircleWithNodes extends Application
{
public void start(Stage PS)
{


final PntPn Points = new PntPn(840, 680);
BorderPane borderPane = new BorderPane(Points);
Scene scene = new Scene(borderPane);
PS.setTitle("Assignment 9");
PS.setScene(scene);
PS.show();
}


    public static void main(String[] args) {
    launch(args);
}





private class PntPn extends Pane
{


final Circle crl = new Circle();
final Vtx[] vtx = new Vtx[3];
final int SW = 6;
final Color CS = Color.BLACK, LS = Color.BLACK;

PntPn(double width, double height)
{
this.setPrefSize(width, height);
this.setWidth(width);
this.setHeight(height);
crl.setStroke(CS);
crl.setFill(Color.TRANSPARENT);
crl.setStrokeWidth(SW);
crl.radiusProperty().bind(this.heightProperty().multiply(0.3));
crl.centerXProperty().bind(this.widthProperty().divide(2));
crl.centerYProperty().bind(this.heightProperty().divide(2));
this.getChildren().add(crl);



for (int k = 0; k < vtx.length; k++)
{
vtx[k] = new Vtx(crl, 2 * Math.PI / vtx.length * (k + Math.random()));
vtx[k].radiusProperty().bind(crl.radiusProperty().divide(8));
vtx[k].stPos();
vtx[k].setFill(Color.BLUE);
vtx[k].setStrokeWidth(SW);
this.getChildren().add(vtx[k]);

vtx[k].setOnMouseDragged(new EventHandler<MouseEvent>()
{
public void handle(MouseEvent e)
{
int k;
for (k = 0; k < vtx.length; k++)
if (vtx[k] == e.getSource())
break;
vtx[k].stAng(e.getX(), e.getY());
mvUpdt((Vtx) e.getSource());
}
});
}

for (int k = 0; k < vtx.length; k++)
{
int m = k + 1 < vtx.length ? k + 1 : 0;
int n = m + 1 < vtx.length ? m + 1 : 0;
vtx[k].bdLg(vtx[m], vtx[n]);
vtx[k].L.setStroke(LS);
vtx[k].L.setStrokeWidth(SW);
this.getChildren().add(vtx[k].L);
this.getChildren().add(vtx[k].txt);
}

for(DoubleProperty DP: new DoubleProperty[]
{crl.radiusProperty(), crl.centerXProperty(), crl.centerYProperty()})
DP.addListener(new RL());
  
mvUpdt(vtx[0]);
}

void mvUpdt(Vtx V)
{
V.stPos();
double[] LL = new double[3];
for (int k = 0; k < vtx.length; k++)
LL[k] = vtx[k].getLL();
  
for (int k = 0; k < vtx.length; k++)
{
int m = k + 1 < vtx.length ? k + 1 : 0;
int n = m + 1 < vtx.length ? m + 1 : 0;
double x = LL[k], y = LL[m], z = LL[n];
double deg = Math.toDegrees(Math.acos((x * x - y * y - z * z) / (-2 * y * z)));
vtx[k].stTxt(deg);
}
}

private class RL implements ChangeListener<Number>
{
  
public void changed(ObservableValue<? extends Number> OV, Number OW, Number NW)
{
for (Vtx V : vtx)
{
V.stPos();
}
}
}
}

private class Vtx extends Circle
{
final Circle crl;
final Line L;
final Text txt;
double centAng;

Vtx(Circle crl, double centAng)
{
this.crl = crl;
this.stAng(centAng);
this.L = new Line();
this.txt = new Text();
this.txt.setFont(Font.font(20));
this.txt.setStroke(Color.BLACK);
this.txt.setTextAlignment(TextAlignment.CENTER);
this.txt.xProperty().bind(this.centerXProperty().add(25));
this.txt.yProperty().bind(this.centerYProperty().subtract(10));
}

double getcentAng() {return this.centAng;}

void stPos()
{
this.setCenterX(crl.getCenterX() + crl.getRadius() * Math.cos(this.getcentAng()));
this.setCenterY(crl.getCenterY() + crl.getRadius() * Math.sin(this.getcentAng()));
}

void stAng(double centAng)
{
this.centAng = centAng;
}

void stAng(double a, double b)
{
this.stAng(Math.atan2(b - crl.getCenterY(), a - crl.getCenterX()));
}

void bdLg(Vtx ver1, Vtx ver2)
{
L.startXProperty().bind(ver1.centerXProperty());
L.startYProperty().bind(ver1.centerYProperty());
L.endXProperty().bind(ver2.centerXProperty());
L.endYProperty().bind(ver2.centerYProperty());
}

double getLL()
{
return Math.sqrt(Math.pow(L.getStartX()-L.getEndX(),2) + Math.pow(L.getStartY()-L.getEndY(),2));
}

void stTxt(double ang)
{
this.txt.setText(String.format("%.0f\u00B0", ang));
}
}
}