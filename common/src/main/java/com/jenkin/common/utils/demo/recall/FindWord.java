package com.jenkin.common.utils.demo.recall;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/7 14:55
 * @description：79. 单词搜索
 * 给定一个二维网格和一个单词，找出该单词是否存在于网格中。
 *
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。
 *
 *
 *
 * 示例:
 *
 * board =
 * [
 *   ['A','B','C','E'],
 *   ['S','F','C','S'],
 *   ['A','D','E','E']
 * ]
 *
 * 给定 word = "ABCCED", 返回 true
 * 给定 word = "SEE", 返回 true
 * 给定 word = "ABCB", 返回 false
 *
 *
 * 提示：
 *
 * board 和 word 中只包含大写和小写英文字母。
 * 1 <= board.length <= 200
 * 1 <= board[i].length <= 200
 * 1 <= word.length <= 10^3
 * @modified By：
 * @version: 1.0
 */
public class FindWord {


    public static void main(String[] args) {
        boolean d = new FindWord().findWord("A", new char[][]{
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        });
        System.out.println(d);
    }


    private boolean findWord(String word,char[][] board){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (findInArr(0, word, board, i, j)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean findInArr(int deep, String word, char[][] board,  int i ,int j) {
        char c = word.charAt(deep);

        //终止条件（已经走过或者超出边界）
        if( i<0||i>=board.length||j<0||j>=board[i].length||board[i][j]=='0'||(board[i][j]!= c )){
            return false;
        }

        if (board[i][j]== c) {
            deep=deep+1;
        }
        if(board[i][j]== c &&deep==word.length()){

            return true;
        }
        char temp = board[i][j];
        board[i][j] = '0';

        //往左走
        boolean b = findInArr(deep, word, board, i, j - 1);
        if (b) return b;
        //往右走
        b|=findInArr(deep,word,board,i,j+1);
        if (b) return b;

        //往上走
        b|=findInArr(deep,word,board,i-1,j);
        if (b) return b;
        //往下走
        b|=findInArr(deep,word,board,i+1,j);
        board[i][j] =  temp;

        return b;
    }


}
