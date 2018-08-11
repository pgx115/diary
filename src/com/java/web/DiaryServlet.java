package com.java.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.dao.DiaryDao;
import com.java.model.Diary;
import com.java.util.DbUtil;
import com.java.util.StringUtil;

public class DiaryServlet extends HttpServlet{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    DbUtil dbUtil=new DbUtil();
    DiaryDao diaryDao=new DiaryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String action=request.getParameter("action");
        if("show".equals(action)){
            diaryShow(request,response);
        }else if("preSave".equals(action)){
            diaryPreSave(request,response);
        }else if("save".equals(action)){
            diarySave(request,response);
        }else if ("delete".equals(action)){
            diaryDelete(request,response);
        }
    }


    private void diaryShow(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String diaryId=request.getParameter("diaryId");
        Connection con=null;
        try{
            con=dbUtil.getCon();
            Diary diary=diaryDao.diaryShow(con, diaryId);
            request.setAttribute("diary", diary);
            request.setAttribute("mainPage", "diary/diaryshow.jsp");
            request.getRequestDispatcher("mainTemp.jsp").forward(request,response);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                dbUtil.closeCon(con);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void diaryPreSave(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String diaryId=request.getParameter("diaryId");
        Connection con=null;
        try{
            if(StringUtil.isNotEmpty(diaryId)){
                con=dbUtil.getCon();
                Diary diary=diaryDao.diaryShow(con, diaryId);
                //int  NewDiary=diaryDao.diaryUpdate(con, diary);
                request.setAttribute("diary", diary);
            }
            request.setAttribute("mainPage", "diary/diarySave.jsp");
            request.getRequestDispatcher("mainTemp.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                dbUtil.closeCon(con);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void diarySave(HttpServletRequest request,HttpServletResponse response){
        String title=request.getParameter("title");
        String content=request.getParameter("content");
        String typeId=request.getParameter("typeId");

        Diary diary=new Diary(title,content,Integer.parseInt(typeId));
        Connection connection=null;

        try {
            connection=dbUtil.getCon();
            int savenum=diaryDao.AddDiary(connection,diary);
            if(savenum>0){
                request.getRequestDispatcher("main?all=true").forward(request, response);
            }else{
                request.setAttribute("diary", diary);
                request.setAttribute("error", "保存失败");
                request.setAttribute("mainPage", "diary/diarySave.jsp");
                request.getRequestDispatcher("mainTemp.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                dbUtil.closeCon(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void diaryDelete(HttpServletRequest request, HttpServletResponse response) {
        String diaryId=request.getParameter("diaryId");
        Connection connection=null;
        try {
            connection=dbUtil.getCon();
            diaryDao.diaryDelete(connection,diaryId);
            request.getRequestDispatcher("main?all=true").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                dbUtil.closeCon(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
