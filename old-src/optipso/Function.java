package optipso;
/*
 * Klasa w ktorej mozna implementowac dowolne funkcje 3D, ktore chcemy poddac optymalizacji
 * Dostaje wartosc x, y do obliczania wartosci i numer funkcji, zeby wiedziec, ktora z zaimplementowych wybrac
*/
public class Function {
      
    public static double functionValue (double x, double y,int fn_id)
  {                        
    switch (fn_id) {    
      /* okregi 1 */
      case  1: x = x*x +y*y; return Math.cos(48*Math.sqrt(x)) -4*x;
      /* okregi 2 */
      case  2: x = x*x +y*y; return Math.cos(96*Math.sqrt(x)) -4*x;
      /* jajka 1 */
      case  3: return 0.5*(Math.cos(31.4*x) +Math.cos(31.4*y)) -4*(x*x +y*y);
      /* jajka 2 */
      case  4: return 0.5*(Math.cos(62.8*x) +Math.cos(62.8*y)) -4*(x*x +y*y);
       /* defaultowy wykres parabola */
      default: return -4*(x*x +y*y);
    }                         
  } 
}
