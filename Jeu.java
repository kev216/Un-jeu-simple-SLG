/*
 * 
 * @author: YIN Xiang, WANG Lingxiao, ZHANG Ying
 * E-mail:  zhangy_1985@hotmail.com
 * 
 */

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.*;
import java.util.Random;

// La classe Piece est la classe mere de toutes les pieces du jeu
// l'attribut position donne la case ou se situe la piece
// l'attribue E est un lien vers l'echiquier
// cela permet d'avoir acces aux autres cases
// l'attribut isBlue permet de tester 
// quelle est couleur de la piece (bleu ou rouge)

abstract class Piece{
    protected Case position;
    protected static Echiquier E;
    public final boolean isBlue;
    int hp;                                   // la valeur de sang
    int exp;                                  // l'experience de caractere                                   // la valeur de attacque

    Piece(Echiquier E, Case c, boolean isBlue){
		this.E=E;
		position=c;
		this.isBlue=isBlue;	
    }
 
    
    // la methode moveOk teste si le deplacement est valide
    // elle teste si la piece a deplacee est de la couleur 
    // de l'equipe qui doit jouer
    // puis elle verifie que la case destination 
    // ne contient pas une piece de la meme equipe : 
    // on ne mange pas les pieces de sa propre equipe

    // cette methode devra etre enrichie par dans les classes filles pour 
    // prendre en compte les regles de deplacement specifique

    public boolean moveOk(Case c){                     // for move
    	return (position.E.getTour()==isBlue) && (c.P==null);
    }
    public boolean attackOk(Case c){                   // for attack
    	return (position.E.getTour()==isBlue) && (c.P!=null&&c.P.isBlue != isBlue);
    	
    }


    public void moveTo(Case c){
    	
    	if (moveOk(c)){
		    position.P=null;
		    position=c;
		    c.P=this;
		    position.E.changeTour();
    	}
    
	    else if(attackOk(c)){
	    	c.P.sub(position.P.getAtt());             //la valeur de attaque d'attaquant transmet a l'adversaire
    		c.P.hp= c.P.getHP();
    		c.P.exp=c.P.getExp();
    		if(c.P.hp<=0)
    		{   
    			add(c.P.exp);
    			c.P=null;
    		}   		
    		position.E.changeTour();  		
    	}    	 
    }


    public abstract Image toImage();
    abstract int getHP();
    abstract int sub(int b);
    abstract int add(int a);
    abstract int getExp();
    abstract int getAtt();    
}



class Pion extends Piece {
   
	int hp=50;
	int exp=2;
	int att=10;
	int def=1;

	
    
    Pion(Echiquier E, Case p,boolean isBlue){
		super(E,p,isBlue);		
    }
    
    public Image toImage(){
    	BufferedImage img=null;
        if (isBlue){
    	try{
    		File f=new File("Images//6.gif");
    		img=ImageIO.read(f);
    	}
    	catch (Exception e)
    	{}
        }
        else{
        	try{
        		File f=new File("Images//6.jpg");
        		img=ImageIO.read(f);
        	}
        	catch (Exception e)
        	{}	
        }
    	return img;
    }
    
  
    
    int getHP()
    {
    	return this.hp;
    }
    int getExp()
    {
    	return this.exp;
    }
    int getAtt(){
    	return this.att;
    }
    
    int add(int a){

    		this.hp=this.hp+a;
        	if (this.hp>50)
        	this.hp=50;
    	
    	return this.hp;
    	
    }
    
    int sub(int b){
    	Random rand=new Random(System.nanoTime());               
    	int pre=rand.nextInt(101);
    	if (pre>10)                                            //la valeur agile, 10% miss
    	{
    	int s=b-this.def;
    	this.hp=this.hp-s;
    	return this.hp;
    	}
    	else
    		return this.hp;
    	
    }

    // Specilisation de ma methode moveOk

    public boolean moveOk(Case c){

		// D'abord on teste si les regles de bases ne sont pas violees
		if(!super.moveOk(c)) return false;
	
		// ensuite on teste les restrictions specifiques au pion :
		// tout d'abord, on ne recule jamais
		// ensuite :
		// soit on se deplace d'une case en avant vers une case vide
		// soit on mange un pion adverse d'un case en travers
		// soit on se deplace la premiere fois de deux cases en avant
		// sans passer au dessus d'une autre piece
	

		if(c.y!=position.y) return false;
		if(isBlue){
		    if(c.x<=position.x) return false;
		    if( c.x >position.x+1) return false;
		}
		else{
		    if(c.x>position.x) return false;
		    if( c.x <position.x-1) return false;
		}   
		return true;
    }
    
    public boolean attackOk(Case c){
  
    	if(!super.attackOk(c)) return false;
    	if(Math.abs(c.x-position.x)==1 && c.y==position.y) return true;
    	if(Math.abs(c.y-position.y)==1 && c.x==position.x) return true;
    	return false;
    	
    }
}

class Roi extends Piece {
	int hp=50 ;
	int exp=0;
	int att=20;
	int def=8;
	Roi(Echiquier E,Case p,boolean isBlue){
	super(E,p,isBlue);
    }
	 public Image toImage(){
	    	BufferedImage img=null;
	        if (isBlue){
	    	try{
	    		File f=new File("Images//3.gif");
	    		img=ImageIO.read(f);
	    	}
	    	catch (Exception e)
	    	{}
	        }
	        else{
	        	try{
	        		File f=new File("Images//3.jpg");
	        		img=ImageIO.read(f);
	        	}
	        	catch (Exception e)
	        	{}	
	        }
    	return img;
    }
    
    int getHP()
    {
    	return this.hp;
    }
    int getExp()
    {
    	return this.exp;
    }
    int getAtt(){
    	return this.att;
    }
    
    int add(int a){
    	this.hp=this.hp+a;
    	if (this.hp>50)
    		return this.hp=50;
    	else
    		return this.hp;
    }
    
    int sub(int b){
    	Random rand=new Random(System.nanoTime());               
    	int pre=rand.nextInt(101);
    	if (pre>30)                                          
    	{
    	int s=b-this.def;
    	this.hp=this.hp-s;
    	if(this.hp<=0){
    		position.P=null;
    		
    		Win W=new Win();                           //si le roi est mort, terminer le jeu
    		W.setVisible(true);
    		W.setTitle("Resultat");
    		W.etiquette.setText(this.color()+" Win");
    		E.setEnabled(false);
    		
    	}
    	return this.hp;
    	}
    	else 
    		return this.hp;
    }
    
    String  color(){
    	if(isBlue) return "Rouge";
    	else return "Bleu";
    }

    // idem que pour le pion
    // le roi a le droit de se deplacer d'une case dans toutes les directions
    public boolean moveOk(Case c){
		if(!super.moveOk(c)) return false;
		if(Math.abs(c.x-position.x)>1 || Math.abs(c.y-position.y)>1) return false;
		return true;
	}
    
    public boolean attackOk(Case c){
    	if(!super.attackOk(c)) return false;
    	if(Math.abs(c.x-position.x)>1 || Math.abs(c.y-position.y)>1) return false;
    	return true;
    }
}

class Tour extends Piece {
	int hp=50 ;
	int exp=4;
	int att=12;
	int def=3;
	Tour(Echiquier E,Case p,boolean isBlue){
	super(E,p,isBlue);
    }
	 public Image toImage(){
	    	BufferedImage img=null;
	        if (isBlue){
	    	try{
	    		File f=new File("Images//1.gif");
	    		img=ImageIO.read(f);
	    	}
	    	catch (Exception e)
	    	{}
	        }
	        else{
	        	try{
	        		File f=new File("Images//1.jpg");
	        		img=ImageIO.read(f);
	        	}
	        	catch (Exception e)
	        	{}	
	        }
    	return img;
    }
    
    int getHP()
    {
    	return this.hp;
    }
    int getExp()
    {
    	return this.exp;
    }
    int getAtt(){
    	return this.att;
    }
    
    int add(int a){
    	this.hp=this.hp+a;
    	if (this.hp>50){
    		return this.hp=50;
    	}
    	else 
    		return this.hp;
    }
    
    int sub(int b){
    	Random rand=new Random(System.nanoTime());
    	int pre=rand.nextInt(101);
    	if (pre>15){
    	int s=b-this.def;
    	this.hp=this.hp-s;
    	return this.hp;         
    	}
    	else return this.hp;    
    }

    // La tour a le droit de se deplace en ligne 
    //ou en colonne sans survoler d'autres pieces
    public boolean moveOk(Case c){
		if(!super.moveOk(c)) return false;
		if(c.x != position.x && c.y != position.y) return false;
	
		for(int i=Math.min(position.x+1,c.x+1);i<Math.max(c.x,position.x);i++)
		    if(E.cases[i][c.y].P!=null) return false;
	
		for(int i=Math.min(position.y+1,c.y+1);i<Math.max(c.y,position.y);i++)
		    if(E.cases[c.x][i].P!=null) return false;
	
		return true;
    }
    
    public boolean attackOk(Case c){
    	if(!super.attackOk(c)) return false;
    	if(c.x != position.x && c.y != position.y) return false;
    	for(int i=Math.min(position.x+1,c.x+1);i<Math.max(c.x,position.x);i++)
		    if(E.cases[i][c.y].P!=null) return false;
    	for(int i=Math.min(position.y+1,c.y+1);i<Math.max(c.y,position.y);i++)
		    if(E.cases[c.x][i].P!=null) return false;
    	return true;
    }

}

class Fou extends Piece {
	int hp=50 ;
	int exp=8;
	int att=16;
	int def=4;
	Fou(Echiquier E,Case p,boolean isBlue){
		super(E,p,isBlue);
    }
	 public Image toImage(){
	    	BufferedImage img=null;
	        if (isBlue){
	    	try{
	    		File f=new File("Images//2.gif");
	    		img=ImageIO.read(f);
	    	}
	    	catch (Exception e)
	    	{}
	        }
	        else{
	        	try{
	        		File f=new File("Images//2.jpg");
	        		img=ImageIO.read(f);
	        	}
	        	catch (Exception e)
	        	{}	
	        }
    	return img;
    }
    
    int getHP()
    {
    	return this.hp;
    }
    int getExp()
    {
    	return this.exp;
    }
    int getAtt(){
    	return this.att;
    }
    
    
    int add(int a){
    
    	this.hp=this.hp+a;
    	if (this.hp>50){
    		return this.hp=50;
    	}
    		return this.hp;
    }
    
    int sub(int b){
    	Random rand=new Random(System.nanoTime());
    	int pre=rand.nextInt(101);
    	if (pre>20){
    	int s=b-this.def;
    	this.hp=this.hp-s;
    	return this.hp;        
    	}
    	else return this.hp;   
    }

    // le fou se deplace en diagonale
    public boolean moveOk(Case c){
		if(!super.moveOk(c)) return false;
		if(Math.abs(position.x-c.x)!=Math.abs(position.y-c.y)) return false;	
	
		int pasX=(position.x<c.x?1:-1);
		int pasY=(position.y<c.y?1:-1);
		for(int i=1;i<Math.abs(position.x-c.x);i++)
		    if(E.cases[position.x+i*pasX][position.y+i*pasY].P!=null)
			return false;
	
		return true;
    }
    
    public boolean attackOk(Case c){
    	if(!super.attackOk(c)) return false;
    	if(Math.abs(position.x-c.x)!=Math.abs(position.y-c.y)) return false;	
    	int pasX=(position.x<c.x?1:-1);
		int pasY=(position.y<c.y?1:-1);
		for(int i=1;i<Math.abs(position.x-c.x);i++)
		    if(E.cases[position.x+i*pasX][position.y+i*pasY].P!=null)
			return false;
    	return true;
    }
}

class Cavalier extends Piece {
	int hp=50 ;
	int exp=6;
	int att=14;
	int def=2;
	Cavalier(Echiquier E,Case p,boolean isBlue){
	super(E,p,isBlue);
	}
    
    public Image toImage(){
    	BufferedImage img=null;
        if (isBlue){
    	try{
    		File f=new File("Images//4.gif");
    		img=ImageIO.read(f);
    	}
    	catch (Exception e)
    	{}
        }
        else{
        	try{
        		File f=new File("Images//4.jpg");
        		img=ImageIO.read(f);
        	}
        	catch (Exception e)
        	{}	
        }
    	
    	return img;
    }
    
    int getHP()
    {
    	return this.hp;
    }
    int getExp()
    {
    	return this.exp;
    }
    int getAtt(){
    	return this.att;
    }
    
    int add(int a){
    	this.hp=this.hp+a;
    	if (this.hp>50)
    		return this.hp=50;
    		return this.hp;
    }
    
    int sub(int b){
    	Random rand=new Random(System.nanoTime());
    	int pre=rand.nextInt(101);
    	if (pre>20){
    	int s=b-this.def;
    	this.hp=this.hp-s;
    	return this.hp;         
    	}
    	else return this.hp;    
    }
 
    // le cavalier peut survoler des pieces
    // il se deplace en faisant des L
    public boolean moveOk(Case c){
		if(!super.moveOk(c)) return false;
		return (Math.abs(position.x-c.x)==2 && Math.abs(position.y-c.y)==1) || (Math.abs(position.x-c.x)==1 && Math.abs(position.y-c.y)==2);
    }
    
    public boolean attackOk(Case c){
    	if(!super.attackOk(c)) return false;
    	return (Math.abs(position.x-c.x)==2 && Math.abs(position.y-c.y)==1) || (Math.abs(position.x-c.x)==1 && Math.abs(position.y-c.y)==2);
    }
   
}

class Reine extends Piece {
	int hp=50;
	int exp=10;
	int att=18;
	int def=5;
	Reine(Echiquier E,Case p,boolean isBlue){
	super(E,p,isBlue);
    }
	 public Image toImage(){
	    	BufferedImage img=null;
	        if (isBlue){
	    	try{
	    		File f=new File("Images//5.gif");
	    		img=ImageIO.read(f);
	    	}
	    	catch (Exception e)
	    	{}
	        }
	        else{
	        	try{
	        		File f=new File("Images//5.jpg");
	        		img=ImageIO.read(f);
	        	}
	        	catch (Exception e)
	        	{}	
	        }
    	return img;
    }
    
    int getHP()
    {
    	return this.hp;
    }
    int getExp()
    {
    	return this.exp;
    }
    int getAtt(){
    	return this.att;
    }
    
    int add(int a){
    	this.hp=this.hp+a;
    	if (this.hp>50){
    		return this.hp=50;
    	}
    	else
    		return this.hp;
    }
    
    int sub(int b){
    	Random rand=new Random(System.nanoTime());
    	int pre=rand.nextInt(101);
    	if (pre>25){
    	int s=b-this.def;
    	this.hp=this.hp-s;
    	return this.hp;         
    	}
    	else return this.hp;   
    }

    // la reine se deplace comme un fou ou comme une tour
    public boolean moveOk(Case c){
		if(!super.moveOk(c)) return false;
		if(c.x != position.x && c.y != position.y && (Math.abs(position.x-c.x)!=Math.abs(position.y-c.y))) return false;
	
		if(c.x == position.x || c.y == position.y){
		    for(int i=Math.min(position.x+1,c.x+1);i<Math.max(c.x,position.x);i++)
		    if(E.cases[i][c.y].P!=null) return false;
		    
		    for(int i=Math.min(position.y+1,c.y+1);i<Math.max(c.y,position.y);i++)
		    if(E.cases[c.x][i].P!=null) return false;
		}else{
		    int pasX=(position.x<c.x?1:-1);
		    int pasY=(position.y<c.y?1:-1);
		    for(int i=1;i<Math.abs(position.x-c.x);i++)
		    if(E.cases[position.x+i*pasX][position.y+i*pasY].P!=null) return false;
		}
		return true;
    }
    
    public boolean attackOk(Case c){
    	if(!super.attackOk(c)) return false;
	    if(c.x != position.x && c.y != position.y && (Math.abs(position.x-c.x)!=Math.abs(position.y-c.y))) return false;
	
		if(c.x == position.x || c.y == position.y){
		    for(int i=Math.min(position.x+1,c.x+1);i<Math.max(c.x,position.x);i++)
		    	if(E.cases[i][c.y].P!=null) return false;
		    
		    for(int i=Math.min(position.y+1,c.y+1);i<Math.max(c.y,position.y);i++)
		    	if(E.cases[c.x][i].P!=null) return false;
		}
		else{
		    int pasX=(position.x<c.x?1:-1);
		    int pasY=(position.y<c.y?1:-1);
		    for(int i=1;i<Math.abs(position.x-c.x);i++)
		    	if(E.cases[position.x+i*pasX][position.y+i*pasY].P!=null) return false;
		}
		return true;
	}
}


// La classe Case gere les cases du damiers
// l'attribut couleur donne la couleur de base de la case : grise ou blanche 
// la case change de couleur lorsque l'on clique dessus 
// ou lorsqu'elle est survolee par la souris avec le click enclenche
// elle reprend sa couleur d'origine lorsque l'on relache le clique
// ou que la souris quitte la case
// le boolean click est vrai si la souris a clique sur la case
// le boolean clicked permet de savoir si le click est enclenche
// l'attribut E contient une reference vers l'echiquier 
// l'attribut P contient une reference vers une piece 
// si la case est occupe par une piece
// les attributs x et y sont les coordonnees de la case dans le damiers
// l'attribut destination sauvegarde la reference de la case 
// vers laquelle on souhaite deplacer un pion
// Case herite de JPanel qui est une sorte d'aire de dessin
// Case implement Mouselistener: l'ecouteur de souris 
// pour recuperer les evenements dus a la souris 

class Case extends JPanel implements MouseListener{ 
    //private final Color couleur;
    private boolean click;
    private static boolean clicked=false;
    public final Echiquier E;
    public Piece P;
    public final int x,y;
    private static Case destination=null;
    public static Case col;            //change le couleur de case pour faire un guide
   

    //Case(Color c, Echiquier E, int x, int y, Piece P){
    Case(Echiquier E, int x, int y, Piece P){
    	// setBackground affecte la couleur de fond
    	//setBackground(c);
    	setOpaque(false);
		// setPreferredSize et setSize permettent de fixer
		// les dimensions de la case
		setPreferredSize(new Dimension(60,60)); 
		setSize(getPreferredSize());
		// les evenements souris sur la case sont ecoutes par la case elle-meme
		addMouseListener(this);
	
		//couleur=c;
		click=false;
		this.E=E;
		this.x=x;
		this.y=y;
		this.P=P;
    }

    // cette fonction remet la case a sa couleur d'origine
    public void ResetColor(){
		setBackground(Color.LIGHT_GRAY);
		setOpaque(false);//*************透明
    }

    // Comme la case ecoute les evenements souris sur elle-meme
    // elle doit implementer les fonctions suivantes
    
    // lorsqu'on enclenche le click sur la case
    public void mousePressed(MouseEvent e){
		click=true; // la case se souvient que la souris a clique sur elle
		clicked=true; // on informe les autres cases que le click est enclenche
		setOpaque(true);//***************不透明
		setBackground(Color.green);  // on change la couleur de la case
		destination=this; // on sauvegarde la reference de la case que la souris survole actuellement avec le click enclenche
		
		//pour faire les guides
		if (destination.P!=null){ 
			for (int i=0;i<10;i++)
				for(int j=0;j<14;j++)
				{	col=E.cases[i][j];
					col.setOpaque(true);//***************不透明
					if (P.moveOk(col))
						col.setBackground(Color.pink);
				    
					if(P.attackOk(col))
						col.setBackground(Color.YELLOW);
				}
	    }
    }

    // evenement "sur click" non utilise
    public void mouseClicked(MouseEvent e) {}
    	
    

    // lorsqu'on entre dans une case avec le click enclenche
    public void mouseEntered(MouseEvent e) {
		if(!click && clicked){ 
		    // on change la couleur 
		    setBackground(Color.cyan);
		    // on met a jour la destination
		    destination=this;
		}
    }
   
    // evenement lorsqu'on relache le click 
    // cet evenement est capte par la case qui a subit le click
    // c'est pourquoi on a besoin de la variable destination
    // elle permet de savoir ou est la souris lorque le click est relacher
    public void mouseReleased(MouseEvent e) {
		// on remet la couleur d'origine de la case
    	ResetColor();
	
		click=false;
		clicked=false;
	
		// on remet la couleur d'origine  de la case destination
		destination.ResetColor();
		
	
		// si la case d'origine contient une piece 
		// on essaie de bouger cette piece vers la case destination
		// ce deplacement reussi si on respecte les contraintes
	
		if(P!=null)
		    P.moveTo(destination);
		for (int i=0;i<10;i++)
		    for (int j=0;j<14;j++){
		    	col=E.cases[i][j];
			    col.ResetColor();
		    }
	
	}


    // Evenement sur sortie de la souris d'une case
    public void mouseExited(MouseEvent e) {
    	if(!click)
    		ResetColor();
	
	}
    

    // la methode paintComponent gere 
    // l'affichage de la case

    public void paintComponent(Graphics g){
		// On affiche d'abord la case
	    super.paintComponent(g); 
		
		// puis on affiche la piece 
		// a l'interieur
		// si la case contient une piece
		if(P!=null){
		    if(P.isBlue)
		    	g.setColor(Color.blue);
		    else
		    	g.setColor(Color.red);
		    Font f = new Font("Comic sans MS",Font.BOLD,20);
		    g.setFont(f);
		    g.drawImage(P.toImage(),5,5,50,50,null);
		    g.fillRect(5, 53,P.getHP(), 6);
		    g.drawRect(5, 53, 50, 6);
		    
		    
		}
    }

}

// la classe echiquier est une fenetre (JFrame)
// qui contient un damier lui-meme constitue de 10X14 cases
// un label "etiquette" qui indique qui doit jouer
// enfin le booleen tour est vrai lorsque c'est le tour des bleus
// et faux lorsque c'est le tour des rouges
// L'echiquier implemente l'ecouteur d'action pour recuperer
// les evenements sur le menu que l'on va creer

class Echiquier extends JFrame implements ActionListener {
    public Case cases[][];
    public Case col;
    private JPanel damier;
    private JLabel etiquette;
    public Piece p;
    
    private boolean tour;
    boolean t=false;    
    
    Echiquier(){
    	super("Jeu");
		setPreferredSize(new Dimension(850,700)); 
		//setBackground(Color.darkGray);
		setIconImage(Toolkit.getDefaultToolkit().createImage("Images//Background.jpg"));
		setSize(getPreferredSize());
		setResizable(false);
		// setLayout permet de choisir la politique de placement
		// des objets graphiques dans un conteneur
		// ici la fenetre
		// on choisi un FlowLayout les elements sont disposes 
		// les uns a la suite des autres
		setLayout(new FlowLayout());
	
		// on choisi de tuer l'application
		// lorsque l'on clique sur la croix
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		// creation d'une barre de menu
		// avec un menu appele "menu"
		// dans ce menu, on a deux choix
		// "Start" pour commencer une partie
		// "Quitter" pour quitter l'application
	 	JMenuBar menubar=new JMenuBar();
		JMenu menu=new JMenu("Menu");	
		JMenuItem it1=new JMenuItem("Start");
	
		// Lorsque l'on clique sur l'item Start, 
		//on declenche une action appelee "Start"
		it1.setActionCommand("Start");
	
		JMenuItem it2=new JMenuItem("Quitter");
		it2.setActionCommand("Quitter");
		
		JMenuItem it3=new JMenuItem("Capituler");
		it3.setActionCommand("Capituler");
	
		// ajout des items au menu
		menu.add(it1);
		menu.add(it2);
		menu.add(it3);
	
		// ajout du menu a la barre de menu
		menubar.add(menu);
	
		// Les evenements sur les items seront ecoutes par l'echiquier
		it1.addActionListener(this);	
		it2.addActionListener(this);
		it3.addActionListener(this);
	
		// on affecte la barre de menu a la fenetre echiquier
		setJMenuBar(menubar);
	
		// on cree une zone de dessin "damier" 
		// avec une politique de placement 
		// en grille 8X8
		// cette zone contiendra les 10X14 cases 
		
		damier=new JPanel(new GridLayout(10,14,0,0)){
				 		public void paintComponent(Graphics g) {
				 			super.paintComponent(g);
				 			ImageIcon img = new ImageIcon("Images//Background.jpg");
				 			g.drawImage(img.getImage(), 0, 0,null);
				 			
				 			g.setColor(Color.white);
				 			//g.drawRect(0, 0, 60, 60);
				 			int w;
				 			int h;
				 			for(int i=0;i<14;i++){
				 			    h=60;
				 			    w=60;
				 			    for(int j=0;j<10;j++)
				 			          g.drawRect(i*w, j*h, w, h);
				 			}
				 			
				 		}
					};
		//damier.setSize(500,500);
		//damier.setBackground(Color.red);
		//damier.setOpaque(false);//*********************透明
		
		etiquette=new JLabel("Aucune partie en cours");
		
		
		// on cree un tableau de cases 10X14
		cases=new Case[10][14];	
	
		// on alterne les couleurs pour obtenir un damier
		// chaque case est ajoutee au damier
		// grace a la politique de placement des objets
		// les 8 premiers case seront placees sur 
		//la premiers ligne du damier, etc
		for(int i=0;i<10;i++)
		    for(int j=0;j<14;j++){
				/*if((i+j)%2==0)
				    cases[i][j]=new Case(Color.lightGray,this,i,j,null);
				else
				    cases[i][j]=new Case(Color.WHITE,this,i,j,null);
				*/
				cases[i][j]=new Case(this,i,j,null);	
				// ajout de la case cree sur le damier
				damier.add(cases[i][j]);
		    }
		
		// on insere le damier dans la fenetre
		add(damier);
		// on insere le label dans la fenetre
		add(etiquette);
		
    }


    // Fonction qui recupere les actions
    // ici les actions correspondent aux evenements
    // sur les items du menu
    
    public void actionPerformed(ActionEvent e){
    	
    	// Si on clique sur Quitter alors on tue l'application
		if(e.getActionCommand().equals("Quitter")){
		    System.exit(0);
		}
		// Sinon, on a clique sur "Start" et on cree une nouvelle partie
		if(e.getActionCommand().equals("Start")){
			NouvellePartie();
		}
		
		if(e.getActionCommand().equals("Capituler")){
			if (t==true){
				Win W=new Win();
				W.setVisible(true);
				W.setTitle("Capituler");
				Piece.E.setEnabled(false);
				if(tour){
					etiquette.setText("Bleu capitule");
					W.etiquette.setText("Bleu capitule");    	
				}
				else{
					etiquette.setText("Rouge capitule");
					W.etiquette.setText("Rouge capitule");    	
				}				
		    }
			else if (t==false){
				etiquette.setText("Commencez SVP");
			}
		}
	}
			
    

    public boolean getTour(){
    	return tour;
    }

    public void changeTour(){ 
		// change le booleen tour et met a jour l'etiquette
		tour=!tour;
		
		if(tour)
		    etiquette.setText("Autour des bleus");
		else
		    etiquette.setText("Autour des rouges");
	
	
    }

    void NouvellePartie(){
    	 t=true;
    		for (int i=0;i<10;i++)
    		    for (int j=0;j<14;j++){
    		    	Case.col=cases[i][j];
    			    Case.col.ResetColor();
    		 }
    	// creation d'une nouvelle partie
		// on affiche "Autour des bleus"
		etiquette.setText("Autour des bleus");
		// les bleus commencent donc
		// tour est initialise a vrai
		tour=true;
	
		// on supprime les pieces de la partie precedente
		for(int i=0;i<10;i++)	
		    for(int j=0;j<14;j++)
			cases[i][j].P=null;
	
		// On place les pieces
	
		for(int i=0;i<7;i=i+2)
		    cases[1][i].P=new Pion(this,cases[1][i],true);
		for(int i=7;i<14;i=i+2)
		    cases[1][i].P=new Pion(this,cases[1][i],true);
		for(int i=0;i<7;i=i+2)
		    cases[8][i].P=new Pion(this,cases[8][i],false);
		for(int i=7;i<14;i=i+2)
		    cases[8][i].P=new Pion(this,cases[8][i],false);
		
		
	
		cases[0][6].P=new Roi(this,cases[0][6],true);
		cases[9][7].P=new Roi(this,cases[9][7],false);
	
		cases[0][1].P=new Tour(this,cases[0][1],true);
		cases[0][12].P=new Tour(this,cases[0][12],true);
	
		cases[9][1].P=new Tour(this,cases[9][1],false);
		cases[9][12].P=new Tour(this,cases[9][12],false);
	
		cases[0][5].P=new Fou(this,cases[0][5],true);
		cases[0][8].P=new Fou(this,cases[0][8],true);
	
		cases[9][5].P=new Fou(this,cases[9][5],false);
		cases[9][8].P=new Fou(this,cases[9][8],false);
	
		cases[0][3].P=new Cavalier(this,cases[0][3],true);
		cases[0][10].P=new Cavalier(this,cases[0][10],true);
	
		cases[9][3].P=new Cavalier(this,cases[9][3],false);
		cases[9][10].P=new Cavalier(this,cases[9][10],false);
	
		cases[0][7].P=new Reine(this,cases[0][7],true);
		cases[9][6].P=new Reine(this,cases[9][6],false);
	
		// une fois toute les pieces placees, on rafraichit l'affichage
		// la partie commence !
		repaint();
		
    }
}

class Win extends JFrame implements ActionListener{
	JLabel etiquette; 
    Win(){
    	setPreferredSize(new Dimension(300,100)); 
    	setBackground(Color.darkGray);
    	setSize(getPreferredSize());

    	setLayout(new FlowLayout());

    	setDefaultCloseOperation(EXIT_ON_CLOSE);

    	// creation d'une barre de menu
    	// avec un menu appele "menu"
    	// dans ce menu, on a deux choix
    	// "ReStart" pour recommencer une partie
    	// "Quitter" pour quitter l'application
     	JMenuBar menubar=new JMenuBar();
    	JMenu menu=new JMenu("Menu");	
    	JMenuItem it1=new JMenuItem("ReStart");

    	// Lorsque l'on clique sur l'item ReStart, 
    	//on declenche une action appelee "ReStart"
    	it1.setActionCommand("ReStart");

    	JMenuItem it2=new JMenuItem("Quitter");
    	it2.setActionCommand("Quitter");

    	// ajout des items au menu
    	menu.add(it1);
    	menu.add(it2);	    

    	// ajout du menu a la barre de menu
    	menubar.add(menu);

    	// Les evenements sur les items seront ecoutes par l'echiquier
    	it1.addActionListener(this);	
    	it2.addActionListener(this);

    	// on affecte la barre de menu a la fenetre echiquier
    	setJMenuBar(menubar);

    	etiquette=new JLabel();
    	// on insere le label dans la fenetre
    	add(etiquette);
    	
    }

    // Fonction qui recupere les actions
    // ici les actions correspondent aux evenements
    // sur les items du menu

    public void actionPerformed(ActionEvent e){	
		// Si on clique sur Quitter alors on tue l'application
		if(e.getActionCommand().equals("Quitter"))
		    System.exit(0);
		else // Sinon, on a clique sur "ReStart" et on cree une nouvelle partie
			Piece.E.setEnabled(true);  
			dispose();                  
			Piece.E.NouvellePartie();
			
    }

}

// le programme principal se contente de creer une fenetre Echiquier
// et de la rendre visible

public class Jeu
{
    public static void main(String [] arg){
		Echiquier E=new Echiquier();
		E.setVisible(true);
		
    }
}