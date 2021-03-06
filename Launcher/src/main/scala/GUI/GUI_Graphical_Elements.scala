import scala.swing._

package GUI{

trait GUI_Graphical_Elements {
	//#Couleurs#//
	//Pour pouvoir utiliser les couleurs en indiquant juste leurs noms (black et pas ???.black)
	val transparent = new Color(0,0,0,0)
	val black = new Color(0,0,0,255)
	val black_dim = new Color(0,0,0,50)
	val white = new Color(255,255,255)
	val blue = new Color(0,0,255)
	val green = new Color(0,200,0)
	val red = new Color(255,0,0)
	val cyan = new Color(0,255,255)
	val purple = new Color(150,0,175)
	val light_green = new Color(50,200,120)
	val light_brown = new Color(200,120,50)
	val color9 = new Color(0,200,200)

	val tan1 = new Color(255,165,79)
	val violet_fluo = new Color(255,0,255)
	val bleu_clair = new Color(50,205,255)
	val deep_pink = new Color(255,20,147)
	val dark_orchid = new Color(153,50,204) //violet
	val dodger_blue = new Color(24,116,205)
	val yellow = new Color(255,255,0)
	val maroon = new Color(176, 48, 96)
	val maroon4 = new Color(139, 28, 98)
	val hunter_green = new Color(142,35,35) //ressemble à maroon4 en plus marron
	val dark_purple = new Color(135,31,120)
	val dark_golden_rod1 = new Color(255,185,15)
	val silver = new Color(230,232,250)
	val silver_grey = new Color(192,192,192)
	val sandy_brown = new Color(244,164,96)
	val firebrick4 = new Color(139,26,26)
	val chocolate4 = new Color(139,69,19)
	val medium_violet_red = new Color(199,21,133)
	val hot_pink = new Color(255,105,180)
	//#Bordures#//Une usine à bordures
	def border(colour: Color = black, thickness: Int = 1) = Swing.LineBorder(colour, thickness)
}

//"GE" -> "Graphical Elements"
object GUI_GE extends GUI_Graphical_Elements{
	//Pour disposer d'une couleur ou d'une bordure sans avoir à hériter de GUI_Graphics
}

}	//Accolade fermante du package GUI
