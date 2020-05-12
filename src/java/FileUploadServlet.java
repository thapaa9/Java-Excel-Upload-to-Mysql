


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Locale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private File fileUploadPath;
    private static final String fileDirectory = "/data";
    public static Connection con;   
    public static PreparedStatement ps;  	
    public static FileInputStream input;
        
	
        File uploadedFile;
    @Override
    public void init() {
        String realPath = this.getServletConfig().getServletContext().getRealPath("/");
        fileUploadPath = new File(realPath + fileDirectory);
        if (!fileUploadPath.exists()) {
            boolean isCreate = fileUploadPath.mkdirs();
            if (isCreate) {
                
            }
        }
    }


        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            ServletContext servletContext = this.getServletConfig().getServletContext();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            DiskFileItemFactory factory = newDiskFileItemFactory(servletContext, repository);

            ServletFileUpload upload = new ServletFileUpload(factory);
           // upload.setHeaderEncoding("utf-8");

            PrintWriter printWriter = response.getWriter();
            //response.setContentType("application/json");
            //response.setCharacterEncoding("utf-8");
            
            try {
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        System.out.println(item.getFieldName());
                    } else { 
                        String name = item.getName();
                        if (!name.equals("")) {
                            int index = name.indexOf("\\");
                                if (index == -1) {
                                uploadedFile = new File(fileUploadPath,
                                        File.separator + name);
                            } 
                            item.write(uploadedFile);
                           try {
                        con=Data.getCon();
                        //File file = new File("C://Users/pashu/Documents/NetBeansProjects/upload/build/web/data/sol.xlsx");     
                        File file = new File(fileUploadPath,File.separator + name);
                        FileInputStream fis = new FileInputStream(file);
			Workbook workbook = new XSSFWorkbook(fis);            
		    
			 Sheet sheet=workbook.getSheetAt(0);        
			 
			 Row row;  
			 DataFormatter formatter = new DataFormatter(Locale.US);
			 for(int i=1; i<=sheet.getLastRowNum(); i++)    
			 { 
				 
				  row=sheet.getRow(i);
                                  ps=con.prepareStatement("insert into sol_details values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");            
				  ps.setString(1,""+formatter.formatCellValue(row.getCell(0)));
				  ps.setString(2,""+formatter.formatCellValue(row.getCell(1)));              
				  ps.setString(3,""+formatter.formatCellValue(row.getCell(2)));      
				  ps.setString(4,""+formatter.formatCellValue(row.getCell(3)));               
				  ps.setString(5,""+formatter.formatCellValue(row.getCell(4)));
                                  ps.setString(6,""+formatter.formatCellValue(row.getCell(5)));              
				  ps.setString(7,""+formatter.formatCellValue(row.getCell(6)));      
				  ps.setString(8,""+formatter.formatCellValue(row.getCell(7)));               
				  ps.setString(9,""+formatter.formatCellValue(row.getCell(8)));
		    		  ps.setString(10,""+formatter.formatCellValue(row.getCell(9)));              
				  ps.setString(11,""+formatter.formatCellValue(row.getCell(10)));      
				  ps.setString(12,""+formatter.formatCellValue(row.getCell(11)));              
				  ps.setString(13,""+formatter.formatCellValue(row.getCell(12)));
                                  ps.setString(14,""+formatter.formatCellValue(row.getCell(13)));               
				  ps.executeUpdate();                
				 
                         }
                         printWriter.print("Uploaded Successfully");
                           }catch (Exception e) {
			e.printStackTrace();
		} 
                       } 
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   
    private DiskFileItemFactory newDiskFileItemFactory(ServletContext context, File repository) {
        FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(context);
        DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
        factory.setFileCleaningTracker(fileCleaningTracker);
        return factory;
    }
    
    
    
}