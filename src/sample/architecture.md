								CLASSE "FORME_BORDURE": 
(classe englobante de DENTS,CREUX,BORDURE_PLATE)
_Attributs de la classe :
	_boolean est_plate (si bordure_droite=true ou dent/creux = false)
	_LineTo ligne_plate (au cas ou est_plate == true)
	_liste de cercle :
		(1) cercles des courbes (nb=7=> 6 courbes)
		(2) cercles des controleurs des courbes (nb=12=6*2)
	_liste de CubicCurveTo(nb=6)
	_liste de MoveTo(nb=6)
	_Path : pour socker nos courbes (cubicCurvesTo + MoveTo)
_Constructeurs:
	_(1) boolean, sans contrainte tout en random
	_(2) creux 



				CLASSE "DENTS": 

_Attributs de la classe :
	
_Constructeurs:
	_(1) sans contrainte tout en random
	_(2) CREUX 

					CLASSE "CREUX": 

_Attributs de la classe :
	
_Constructeurs:
	_(1) sans contrainte tout en random
	_(2) DENTS 







	EXTENSION POSSIBLE:
	_cree des pieces ayant plusieurs creux ou dents sur un seul coté

