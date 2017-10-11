package cn.simma.bookstore.book.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;
import cn.simma.bookstore.book.domain.Book;
import cn.simma.bookstore.book.service.BookService;
import cn.simma.bookstore.category.domain.Category;
import cn.simma.bookstore.category.service.CategoryService;

public class AdminBookAddServlet extends HttpServlet {
	CategoryService categoryService=new CategoryService();
	BookService bookService=new BookService();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
		//创建工厂，设置缓存大小和临时缓存目录
		DiskFileItemFactory dfif=new DiskFileItemFactory(20*1024,new File("F:/f/temp"));
		ServletFileUpload sfu=new ServletFileUpload(dfif);
		// 设置单个文件大小为15KB
		sfu.setFileSizeMax(20*1024);
			List<FileItem> fileItemList=sfu.parseRequest(request);
			/*
			 * * 把fileItemList中的数据封装到Book对象中
			 *   > 把所有的普通表单字段数据先封装到Map中
			 *   > 再把map中的数据封装到Book对象中
			 */
			Map<String,String> map=new HashMap<String,String>();
			for(FileItem f:fileItemList){
				if(f.isFormField()){
					map.put(f.getFieldName(),f.getString("UTF-8"));
				}
			}
			Book book=CommonUtils.toBean(map,Book.class);
			book.setBid(CommonUtils.uuid());
			/*
			 * 需要把Map中的cid封装到Category对象中，再把Category赋给Book
			 */
			Category category=CommonUtils.toBean(map,Category.class);
			book.setCategory(category);
			/*
			 * 2. 保存上传的文件
			 *   * 保存的目录
			 *   * 保存的文件名称
			 */
			String savePath=this.getServletContext().getRealPath("/book_img");
			String fileName=CommonUtils.uuid()+"_"+fileItemList.get(1).getName().toLowerCase();
			System.out.println(savePath);
			
			/*
			 * 校验文件的扩展名
			 */
			if(!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")|| fileName.toLowerCase().endsWith(".png")) ){
				request.setAttribute("msg","您上传的图片不为jpg格式，请重新上传！");
				request.setAttribute("categoryList",categoryService.findByAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			// 使用目录和文件名称创建目标文件
			File destFile=new File(savePath,fileName);
			// 保存上传文件到目标文件位置
			fileItemList.get(1).write(destFile);
			/*
			 * 3. 设置Book对象的image，即把图片的路径设置给Book的image
			 */
			book.setImage("book_img/"+fileName);
			/*
			 * 4. 使用BookService完成保存
			 */
			bookService.add(book);			
			/*
			 * 校验图片的尺寸
			 */
			Image image=new ImageIcon(destFile.getAbsolutePath()).getImage();
			if(image.getWidth(null)>200||image.getHeight(null)>200){
				destFile.delete();//删除这个文件！
				request.setAttribute("msg","您上传的图片超出200*200！");
				request.setAttribute("categoryList",categoryService.findByAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			/*
			 * 5. 返回到图书列表
			 */
			request.getRequestDispatcher("/admin/AdminBookServlet?method=findAll").forward(request, response);
		} catch (Exception e) {
			if(e instanceof FileUploadBase.FileSizeLimitExceededException){
				request.setAttribute("msg","您上传的图片超出20K!!!");
				request.setAttribute("categoryList",categoryService.findByAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				
			}
			e.printStackTrace();
		}
		
		
	}

}
