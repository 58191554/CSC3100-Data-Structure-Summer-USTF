import java.io.*;
import java.util.*;

public class ass1 {

    public static String compact(String exp_str){
        String compact_str = "";
        for(int i = 0;i<exp_str.length();i++){
            if(exp_str.charAt(i) !=' ')
                compact_str+=exp_str.charAt(i);
        }
        return compact_str;
    }

    // splite the terms with add(+) and minus(-)
    public static ArrayList<String> get_terms (String exp_str){
        
        ArrayList<String> terms = new ArrayList<String>();
        Stack<Integer> brak_st = new Stack<Integer>();
        int pt_1 = 0; int pt_2 = 0;
        boolean valid = true;
        for(int i = 0; i< exp_str.length();i++){
            if(exp_str.charAt(i) == '(')
                brak_st.push(i);
            else if(exp_str.charAt(i) == ')'){
                if(!exp_str.isEmpty())
                    brak_st.pop();
                else{
                    valid = false;
                    break;
                }
            }

            if((exp_str.charAt(i) == '+' || exp_str.charAt(i) == '-') && brak_st.isEmpty()){
                pt_2 = i;
                terms.add(exp_str.substring(pt_1, pt_2));
                pt_1 = i;
            }
        }
        if(!brak_st.isEmpty())  valid = false;
        terms.add(exp_str.substring(pt_2,exp_str.length()));
        if(valid == false){
            terms.clear();
            terms.add("invalid");
        }
        return terms;
    }

    public static boolean judge_need_brk(String derv_term){
        int len = derv_term.length();
        boolean need_brk = false;
        if(len == 0)
            return need_brk;
        return need_brk;
    }

    public static String deal_func(String term, String derv_term, int r_pos, int l_pos){

        String sub_deriv = "";
        String func;

        try{
            func = term.substring(l_pos-3, l_pos);
        }
        catch(Exception e){
            return sub_deriv;
        }
        boolean need_brk = judge_need_brk(derv_term);

        if(func.equals("sin") ){
            if(!derv_term.equals(""))
                sub_deriv+="*";
            sub_deriv+="cos";
            sub_deriv+=term.substring(l_pos, r_pos);  
            sub_deriv+=auto_bracket(sub_deriv);
        }

        if(func.equals("cos") ){
            if(!derv_term.equals(""))
                sub_deriv+="*";
            if(need_brk==true)
                sub_deriv+="(";
            sub_deriv+="-sin";
            sub_deriv+=term.substring(l_pos, r_pos);  
            sub_deriv+=auto_bracket(sub_deriv);
        }
        return sub_deriv;
    }

    public static String auto_bracket(String term){
        int brk_num = 0;
        String brackets = "";
        for(int i = 0; i<term.length();i++){
            if(term.charAt(i) == '(')
                brk_num++;
            if(term.charAt(i) == ')')
                brk_num--;
        }
        try{
            for(int i = 0; i<brk_num;i++)
                brackets+=")";
            return brackets;
        }
        catch(Exception e){
            return "";
        }
    }

    public static String deal_mul_pwr(String term, String derv_term, 
    int r_pos, int l_pos, int pre_r_pos, int pre_l_pos){

        String sub_derv_term = "";
        String front_sub;
        String back_sub ;
        try{
            front_sub = term.substring(pre_l_pos, l_pos);
            back_sub = term.substring(r_pos, pre_r_pos+1);
        }
        catch(Exception e){
            return "";
        }
        
        
        int idx_mul = front_sub.indexOf("*");
        int idx_pwr = back_sub.indexOf("^");
        int mul_c = 1;
        int pwr_c = 1;

        boolean negative = false;

        if(idx_mul != -1){
            int mul_head = idx_mul-1;
            String mul_str = "";
            while(mul_head >=0){
                if(front_sub.charAt(mul_head) == '.'||
                Character.isDigit(front_sub.charAt(mul_head))||
                front_sub.charAt(mul_head) == '-'){
                    mul_str = front_sub.charAt(mul_head)+mul_str;
                    if(front_sub.charAt(mul_head) == '-'){
                        negative = true;
                        break;
                    }
                    mul_head--;
                }
                else
                    break;
            }
            try{
                if(!mul_str.equals("")) mul_c = Integer.parseInt(mul_str);
            }
            catch(Exception e){
                System.out.println("bad end: ");
                System.out.println( mul_c);
            }
        }

        if(idx_pwr != -1){
            int pwr_tail = idx_pwr+1;
            String pwr_str = "";
            while(pwr_tail <= back_sub.length()-1){
                if(back_sub.charAt(pwr_tail) == '.'||Character.isDigit(back_sub.charAt(pwr_tail))){
                    pwr_str += back_sub.charAt(pwr_tail);
                    pwr_tail++;
                }
                else
                    break;
            }
            if(!pwr_str.equals("")) pwr_c = Integer.parseInt(pwr_str);
        }
        int new_mul = mul_c*pwr_c;
        int new_pwr = pwr_c-1;
        
        if(new_mul == 0){
            return sub_derv_term;
        }

        if(front_sub.length()!=0 && front_sub.charAt(0) == '-' && new_mul>0)  new_mul = -new_mul;
        if(new_pwr==0){
            if(new_mul == 1)    return "";
            if(derv_term.length() == 0)    sub_derv_term+=new_mul;
            else{
                sub_derv_term+="*";
                sub_derv_term+=new_mul;
            }
            return sub_derv_term;
        }

        if(derv_term.length()!=0)   sub_derv_term+="*";

        sub_derv_term+=Integer.toString(new_mul);
        sub_derv_term+="*";
        if(front_sub.contains("cos"))sub_derv_term+="cos";
        if(front_sub.contains("sin"))sub_derv_term+="sin";
        if(term.substring(l_pos, r_pos).equals("")) sub_derv_term+="x";
        else sub_derv_term+=term.substring(l_pos, r_pos);
        sub_derv_term+=auto_bracket(sub_derv_term);

        if(new_pwr == 1){
            return sub_derv_term;
        }
        sub_derv_term+="^";
        sub_derv_term+=Integer.toString(new_pwr);
        sub_derv_term+=auto_bracket(sub_derv_term);
        return sub_derv_term;
    }

    public static String deal_term(String term){
        // mark the bracket index in stack
        String derv_term = "";
        Queue<Integer> l_brak_st = new LinkedList<Integer>();
        Queue<Integer> r_brak_st = new LinkedList<Integer>();
        int pt_1 = 0;
        int pt_2 = term.length()-1;
        int pre_r_pos = term.length()-1;
        int pre_l_pos = 0;

        if(term.equals("")) return "";

        if(term.indexOf("(") == -1){
            int l_pos = term.indexOf("x");
            int r_pos = term.indexOf("x")+1;
            derv_term+=deal_mul_pwr(term,derv_term,r_pos,l_pos,pre_r_pos,pre_l_pos);
            derv_term += deal_func(term, derv_term, r_pos, l_pos);
  
            return derv_term;
        }

        while(pt_1<pt_2 && 
        (term.substring(pt_1,pt_2).indexOf("(")!=-1 || term.substring(pt_1,pt_2).indexOf(")")!=-1)){
            // with no bracket

            // with bracket, need to chain rule 
            if(term.charAt(pt_1) == '(' && r_brak_st.isEmpty()){
                if(l_brak_st.isEmpty())         l_brak_st.add(pt_1);
                if(l_brak_st.peek() != pt_1)    l_brak_st.add(pt_1);
            }
            else if(term.charAt(pt_1) == '(' && !r_brak_st.isEmpty()){
                int r_pos = r_brak_st.remove();
                int l_pos = pt_1;

                derv_term += deal_mul_pwr(term,derv_term, r_pos, l_pos, pre_r_pos, pre_l_pos);
                derv_term += deal_func(term, derv_term, r_pos, l_pos);
                pre_r_pos = r_pos;
                pre_l_pos = l_pos+1;
                pt_2--;
            }
            if(term.charAt(pt_2) == ')' && l_brak_st.isEmpty()){
                if(r_brak_st.isEmpty())
                    r_brak_st.add(pt_2);
                if(r_brak_st.peek()!=pt_2)
                    r_brak_st.add(pt_2);
            }        
            else if(term.charAt(pt_2) == ')' && !l_brak_st.isEmpty()){
                int r_pos = pt_2;
                int l_pos = l_brak_st.remove();

                derv_term += deal_mul_pwr(term,derv_term, r_pos, l_pos, pre_l_pos, pre_r_pos);
                derv_term += deal_func(term, derv_term, r_pos, l_pos);

                pre_r_pos = r_pos;
                pre_l_pos = l_pos;
            }

            if(l_brak_st.isEmpty()) pt_1++;
            if(r_brak_st.isEmpty()) pt_2--;
        }
        derv_term+= x_mul_pwr(term, derv_term, pre_r_pos, pre_l_pos);
        derv_term+=auto_bracket(derv_term);
        return derv_term;
    }

    public static String x_mul_pwr(String term, String derv_term, int pre_r_pos, int pre_l_pos){
        String sub_term = term.substring(pre_l_pos,pre_r_pos);
        int idx_x = pre_l_pos+sub_term.indexOf("x");
        return deal_mul_pwr(term, derv_term, idx_x+1, idx_x, pre_r_pos, pre_l_pos);
    }

    public static String derivative(String exp_str){
        String compact_str = compact(exp_str);
        System.out.println(compact_str);
        ArrayList<String> termList = get_terms(compact_str);
        String ans = "";
        for(String term: termList){
            String new_term = "";
            if(term.equals("invalid"))
                return "invalid";
            try{
                new_term = deal_term(term);
            }
            catch(Exception e){
                new_term = "";
            }
            if(new_term.length()!=0&& new_term.charAt(0)!='-')
                new_term = "+"+new_term;
            System.out.println(new_term);
            ans+=new_term;
        }
        if(ans.equals(""))  return ans;
        if(ans.charAt(0) == '+')
            ans = ans.substring(1);
        return ans;
    }

    // public static void main(String[] args) {
    //     String str = "4*x^5+6";
    //     System.out.print("result>>>" + derivative(str));
    // }    
    public static void main(String[] args) throws Exception {
        FileReader freader = new FileReader(args[0]);
        BufferedReader breader = new BufferedReader(freader);
        FileWriter fwriter = new FileWriter(args[1], false);
        BufferedWriter bwriter = new BufferedWriter(fwriter);
        String str = breader.readLine();
        while (true) {
            String result = derivative(str);
            bwriter.write(result);
            str = breader.readLine();
            if (str == null) break;
            bwriter.newLine();
        }
        freader.close();
        breader.close();
        bwriter.flush();
        bwriter.close();
    }
}
