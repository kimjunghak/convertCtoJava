/**
 * Created by KJH on 2016-09-11.
 * OS : Windows 7 Ultimate
 * Compiler : Intellij 2016.2.4(JAVA), Visual Studio2015(C)
 */

import java.io.*;

public class test{
    public static void main(String args[]) throws IOException{
        FileInputStream input = new FileInputStream("C:/Users/KJH/IdeaProjects/compiler/file/src/test.ad");
        FileWriter writer = new FileWriter("C:/Users/KJH/IdeaProjects/compiler/file/src/test.c");
        StringBuffer buffer = new StringBuffer();
        int data, def=0, reduce=0, print=0, count=0, op=0, operand=0;
        int[] arr = new int[5];
        String var_name[] = new String[5];
        String operator = null;

        // C언어 초기 선언
        String c_code;
        c_code = "#include <stdio.h> \r\n";
        writer.write(c_code);
        c_code = "int main(){ \r\n";
        writer.write(c_code);

        while(true){
            if((data = input.read()) == -1)
                break;

            String ch_data = String.valueOf((char)data);

            // Token 으로 나누는 if문
            if (ch_data.equals("("))
                buffer.setLength(0);
            // ( 건너뛰기

            else if (ch_data.equals(",")) {
                arr[count++] = Integer.parseInt(buffer.toString());
                buffer.setLength(0);
            }
            // , 일때는 리스트에서 Integer 값들이 나오기 때문에 arr배열에 읽은 순서대로 저장

            else if (ch_data.equals(" ")) {
            }
            // 공백(space) 건너뛰기

            else if (ch_data.equals(")"))
                var_name[1] = buffer.toString();
            // ) 괄호가 오면 한줄이 끝난것이기 때문에 마지막에 오는 변수명을 저장

            else if (data == 10 || data == 13) { // Enter 인식 - 유니코드 10번이 LF(Line Feed) 즉 New Line, 13번은 CR(carriage return)
            }

            else if (ch_data.equals("[")) {
                var_name[0] = buffer.toString();
                buffer.setLength(0);
            }

            // [ 는 리스트의 시작하기 전 리스트의 변수명을 buffer에 저장해 놨기 때문에 리스트의변수명을 저장

            else if (ch_data.equals("]")) {
                arr[count++] = Integer.parseInt(buffer.toString());
                buffer.setLength(0);
            }

            // ] 는 리스트가 끝난 것이기 때문에 마지막 Integer값을 저장

            else if (ch_data.equals("+") || (ch_data.equals("*"))) {
                if(ch_data.equals("+"))
                    operator = "+";
                if(ch_data.equals("*"))
                    operator = "*";
                op = 1;
                buffer.setLength(0);
            }

            // op가 + 나 * 일때 + 일떄는 0부터 더해야 리스트의 값들일 정확히 다 더할 수 있다.
            // * 일때는 1부터 곱해야 더 곱할 수 있다.

            else {
                buffer.append(ch_data);
                if(op==1) { // operator 다음 오는 숫자를 따로 추출
                    operand = Integer.parseInt(buffer.toString());
                    buffer.setLength(0);
                    op = 0;
                }
            }
            // 다 나누고 난 뒤

            // 어떤 명령인지 파악
            if(buffer.toString().equals("def")) {
                def = 1;
                buffer.setLength(0);
            }
            else if(buffer.toString().equals("reduce")) {
                reduce = 1;
                buffer.setLength(0);
            }
            else if(buffer.toString().equals("print")) {
                print = 1;
                buffer.setLength(0);
            }
        }

        // 파일을 다 읽고 난뒤 C파일을 만들기 위해 파일에 쓰는 과정
        if(def == 1) {
            c_code = "\tlong " + var_name[0] + "[] = {";
            writer.write(c_code);
            for (int i = 0; i < count - 1; i++) {
                c_code = arr[i] + ", ";
                writer.write(c_code);
            }
            c_code = arr[count - 1] + "}; \r\n\tint _AD_size = " + count + ";\r\n";
            writer.write(c_code);
        }
        if(reduce == 1){
            c_code = "\tint "+ var_name[1] + "= "+ operand +", _AD_i=0; \r\n\tfor(_AD_i=0 ; _AD_i < " + count +" ; _AD_i++){ \r\n\t\t"
                        + var_name[1] + " " + operator + "= " + var_name[0] + "[_AD_i];\r\n\t}\r\n";
            writer.write(c_code);
        }
        if(print == 1){
            var_name[2] = buffer.toString();
            c_code = "\tprintf(\"%d\"\n," + var_name[2] + ");\r\n\treturn 0;\r\n}";
            writer.write(c_code);
        }

        input.close();
        writer.close();
    }
}
