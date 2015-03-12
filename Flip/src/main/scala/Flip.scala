import scala.swing._
import scala.swing.event._
//import scala.swing.BorderPanel.Position._
import java.util.{Date, Locale}
import java.text.DateFormat
import java.text.DateFormat._
import java.text.SimpleDateFormat
import scala.math._
//import java.awt.event.{ActionEvent, ActionListener}
//import javax.swing.{ImageIcon, Icon}

//"FGE" -> "Flip_Graphical_Element"
object FGE extends GUI_Graphical_Elements{
	val grey = new Color(96,96,96)
	val light_grey = new Color(160,160,160)

	def no_color_mode () = {
		//Le max est une sécurité. Si IndexOf ne trouve pas la chaine correspondant au mode de couleur dans la liste de ses valeurs possibles, il renvoie -1.
		//Ainsi, en cas de faute de frappe, le mode de couleur utilisé est le Normal
		max(0,Flip.string_game_parameters_def_list(1)._3.indexOf(Flip.string_game_parameters_def_list(1)._2))
	}

	def label_color_black () = {
		label_color_black_list(no_color_mode())
	}
	def label_color_white () ={
		label_color_white_list(no_color_mode)
	}

	val label_color_black_list = Vector(grey)
	val label_color_white_list = IndexedSeq(white)

	val bottom_panel_color_list = IndexedSeq(grey)

}

class Flip_Help_Frame extends Frame {
	title = "Help"
	contents = new Label("Flip the squares by clicking at them so that they are all white")
	visible = true
}

class Flip_About_Frame extends Frame{
	title = "About"
	contents = new Label("Graphical Interface by G.Hocquet and T.Dupriez")
	visible = true
}

object Flip extends Game{
	val title = "Flip"

	val square_size_x = 50
	val square_size_y = 50
	var game_beginning_time: Date = null
	//var in_game = false héritée de Game

	//##Game parameters##
	var numeric_game_parameters_def_list = IndexedSeq(("Width", 0, 3, 25), ("Height", 0, 3, 25), ("Starting Flips", 0, 10, 10))
	var string_game_parameters_def_list = IndexedSeq(("Shape Type", "Crosses", IndexedSeq("Crosses", "Random")), ("Colour Mode", "Classic", IndexedSeq("Classic")))
	def nb_of_rows = numeric_game_parameters_def_list(1)._2  //fait de nb_of_rows un alias de la valeur du paramètre Height (ne marche que pour la lecture)
	def nb_of_cols = numeric_game_parameters_def_list(0)._2  //fait de nb_of_cols un alias de la valeur du paramètre Width (ne marche que pour la lecture)
	def nb_of_starting_flips = numeric_game_parameters_def_list(2)._2
	def shape_type = string_game_parameters_def_list(0)._2
	def color_parameter = string_game_parameters_def_list(1)._2
		
	//Conservé pour futurs références mais inutile dans le démineur
	/*def nb_of_bombs = game_parameter_1 //Ces deux fonctions font de nb_of_bombs un alias de la variable game_parameter_1
	def nb_of_bombs_=(newval: Int) { game_parameter_1 = newval }*/

	type Game_Label_Class = Flip_Label
	def glb_factory () = { new Game_Label_Class } // "glb" -> "Game_Label_Class"
	def about_frame_factory () = { new Flip_About_Frame }
	def help_frame_factory () = { new Flip_Help_Frame }

	//var random_gen héritée de Game
	//var game_frame_content héritée de Game

	val game_game_mode_list = IndexedSeq(
		Game_Mode(IndexedSeq(3,3,10),IndexedSeq("Crosses", "Classic")),
		Game_Mode(IndexedSeq(4,4,10),IndexedSeq("Crosses", "Classic")),
		Game_Mode(IndexedSeq(5,5,10),IndexedSeq("Crosses", "Classic"))
	)
	def custom_game_parameters_conditions (form_nb_fields_result: IndexedSeq[Int]) ={ //form_nb_fields_result(0) = nb_of_cols, form_nb_fields_result(1) = nb_of_rows, form_nb_fields_result(2) = nb_of_bombs
		//val return_value = form_nb_fields_result(1) * form_nb_fields_result(0) > 9 && form_nb_fields_result(2) + 9 <= form_nb_fields_result(1) * form_nb_fields_result(0)
		var return_value = "OK"
		if (form_nb_fields_result(1) * form_nb_fields_result(0) <= 9) 
			return_value = "Grille trop petite"
		if (form_nb_fields_result(2) + 9 > form_nb_fields_result(1) * form_nb_fields_result(0))
			return_value = "Pas assez de place dans la grille pour les mines"
		return_value
				
	}	

	def game_starter () = {
		Flip.game_begun = false
		game_frame_content.bottom_panel.background = DGE.bottom_panel_color_list(DGE.no_color_mode)
		nb_of_white_square = nb_of_rows * nb_of_cols
		board = List()
		//Représente le plateau d'une partie: Une matrice de couples (Couleur, Cases_voisines_sous_influence)
		//Convention: La matrice est un tableau ligne (x) de tableaux colonnes (y)
		//Couleur est un booleen: true->White, false->Black
		//Cases_voisines_sous_influences est une List de 8 booleen correspondant aux cases voisines et indiquant si ces cases doivent
		//etre retournée lorsque la case centrale est cliquée
		//Convention: Les cases sont comptées de gauche à droite et de haut en bas
		var infl_list : List[Boolean] = List()
		for (x <- 0 until nb_of_rows){
			var x_col: List[(Boolean, List[Boolean])] = List()
			for (y <- 0 until nb_of_cols){
				infl_list = List()
				if (shape_type == "Crosses") {
					infl_list = /*(x < (nb_of_cols - 1) && y < (nb_of_rows - 1))*/ false :: infl_list	//bottom right
					infl_list = (y < (nb_of_rows - 1)) :: infl_list							//bottom
					infl_list = /*(x > 0 && y < (nb_of_rows - 1))*/ false :: infl_list				//bottom left
					infl_list = (x < (nb_of_cols - 1)) :: infl_list							//right
					infl_list = (x > 0) :: infl_list										//left
					infl_list = /*(x < (nb_of_cols - 1) && y > 0)*/ false :: infl_list				//top right
					infl_list = (y > 0) :: infl_list										//top
					infl_list = /*(x > 0 && y > 0)*/ false :: infl_list								//top left
				}
				var xy_case: (Boolean,List[Boolean]) = (true,infl_list)
				x_col = xy_case :: x_col
			}
			board = x_col.reverse :: board
		}
		board = board.reverse
		//board printer
		/*println(board.length)
		 for (i <- 0 until nb_of_cols) {
		 for ( j <- 0 until nb_of_rows) {
			print(" " + board(i)(j));
		 }
		 println();
		}*/

		//Applique des flips au board (autant que spécifié dans les paramètres du jeu) avant que le joueur puisse jouer
		while (nb_of_white_square == nb_of_rows * nb_of_cols) {
			for (i <- 1 to nb_of_starting_flips) {
				var random_x = random_gen.nextInt(nb_of_cols-1)
				var random_y = random_gen.nextInt(nb_of_rows-1)
				flip(random_x, random_y)
			}
		}
		Flip.initial_board = Flip.board
		//Initialise chaque label selon le board
		for (y <- 0 until nb_of_rows) {
			for (x <- 0 until nb_of_cols) {
				val case_colour = board(x)(y)._1
				val case_infl_list = board(x)(y)._2
				game_frame_content.grid.access_xy(x,y).init(case_colour, case_infl_list)
			}
		}

	}
	def game_action_restart() : Unit = {
		if (Flip.in_game) {
			Flip.game_frame_content.timer_label.stop() //Le timer ne fait que se réinitialiser
			Flip.game_begun = false
			Flip.board = Flip.initial_board
			//Initialise chaque label selon le board
			for (y <- 0 until nb_of_rows) {
				for (x <- 0 until nb_of_cols) {
					val case_colour = board(x)(y)._1
					val case_infl_list = board(x)(y)._2
					game_frame_content.grid.access_xy(x,y).init(case_colour, case_infl_list)
				}
			}			
		}


	}
	//Définit ce qui se passe en cas de victoire du joueur -> voir Game
	override def win() = {
		super.win()		
	}
	//Définit ce qui se passe en cas de défaite du joueur -> voir Game
	override def lose() = {
		super.lose()
	}

	//##Flip Variables## // Variables internes au Démineur
	var board: List[List[(Boolean, List[Boolean])]] = List()
	var initial_board: List[List[(Boolean, List[Boolean])]] = List()
	var nb_of_white_square = 0
	var game_begun = false
	//##Flip Functions## //Fonctions internes au Démineur
	//turn change la couleur de la case (x,y) dans board
	def turn (x: Int, y: Int) = {
		//println("a_turn called with: " + x + ", " + y)
		val previous_color = board(x)(y)._1
		if (previous_color) {	//la case était blanche
			//board(x)(y)._1 = false
			board = board.updated(x, board(x).updated(y, (false, board(x)(y)._2)))
			game_frame_content.grid.access_xy(x,y).turn()
			nb_of_white_square = nb_of_white_square - 1
		}
		else {					//la case était noire
			//board(x)(y)._1 = true
			board = board.updated(x, board(x).updated(y, (true, board(x)(y)._2)))
			nb_of_white_square = nb_of_white_square + 1
			game_frame_content.grid.access_xy(x,y).turn()
		}
	}
	//Renvoie les coordonnées de la case numéro i dans la liste d'influence de la case (x_base, y_base)
	def neighbour_square_xy (i: Int, x_base: Int, y_base: Int) ={
		var x_result = x_base
		var y_result = y_base
		def xpp () ={x_result = x_result + 1}
		def xmm () ={x_result = x_result - 1}
		def ypp () ={y_result = y_result + 1}
		def ymm () ={y_result = y_result - 1}
		i match {
			case 0 => {xmm; ymm}
			case 1 => {ymm}
			case 2 => {xpp; ymm}
			case 3 => {xmm}
			case 4 => {xpp}
			case 5 => {xmm; ypp}
			case 6 => {ypp}
			case 7 => {xpp; ypp}
		}
		(x_result, y_result)
	}
	//flip applique le résultat d'un clic sur la case (x,y) de board
	def flip (x: Int, y: Int) ={
		//println("a_flip called with: " + x + ", " + y)
		//println("infl_list: " + board(x)(y)._2)
		val infl_list = board(x)(y)._2
		for (i <- 0 to 7) {
			if (infl_list(i) == true) {
				val square_to_turn = neighbour_square_xy(i, x, y)
				turn(square_to_turn._1, square_to_turn._2)
			}
		}
		turn(x,y)
		check_win()
	}

	def check_win () ={
		if (nb_of_white_square == nb_of_rows * nb_of_cols && in_game) {
			Flip.win()
		}
	}
}


object Main {
	def main(args: Array[String]) {
		val ui = new UI(Flip)
		ui.visible = true
	}
}