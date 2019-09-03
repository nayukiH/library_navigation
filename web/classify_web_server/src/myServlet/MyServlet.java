package myServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//��ǰҳ�����ڷ���������·��
	private String project;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	/*
	 * ��׿���ϴ�ͼƬHTTP���󷽷�Ϊpost
	 * doPost�����н���ͼƬ�뱣�棬ʹ��ģ�ͽ���ͼ��ʶ��
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//���ñ���s
		
		response.setContentType("text/html,charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		
		//�õ�������
		InputStream in=request.getInputStream();
		System.out.println(in);
		int length=request.getContentLength();
		
		project=request.getSession().getServletContext().getRealPath("/");
		String model=project+"WEB-INF"+File.separator+"models";
		
		System.out.println(model);
		
		try {
			String upload;
			String finalLabel;
			
			
			upload = convertStreamToString(in,"UTF-8",length);
			finalLabel=LabelImage.excute(upload,model);//��ȡģ�ͳ���
			System.out.println(finalLabel);
			out.write(finalLabel);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
								
		
	}
	
	//��inputStreamת���ַ���
	private String convertStreamToString(InputStream in,String charSet,int length) throws Exception, IOException {
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		byte [] data=new byte[length];
		int count=-1;
		while((count=in.read(data,0,length))!=-1) {
			out.write(data,0,length);
		
	}
		data=null;
		return new String(out.toByteArray(),charSet);
	}
}
	


