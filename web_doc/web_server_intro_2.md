# 服务器搭建说明文档（二）

### src新建servlet

- 右键点击src->new->servlet, 确定servlet的名称后一直next，到达此页面时

  ![..\images\servlet_1.PNG]()

  可对servlet中的方法根据你的服务器功能进行选择，我选择的是doPost, doGet

- 我决定要写post方法，因为安卓端是通过http的post请求将图片上传至服务器

  代码如下

  ```
  /*
  	 * 安卓端上传图片HTTP请求方法为post
  	 * doPost方法中接受图片请保存，使用模型进行图像识别
  	 */
  	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		// TODO Auto-generated method stub
  		//设置编码s
  		
  		response.setContentType("text/html,charset=UTF-8");
  		request.setCharacterEncoding("utf-8");
  		response.setCharacterEncoding("utf-8");
  		PrintWriter out=response.getWriter();
  		
  		//得到输入流
  		InputStream in=request.getInputStream();
  		System.out.println(in);
  		int length=request.getContentLength();
  		
  		//得到当前项目运行的路径，方便之后tensorflow模型的调用
  		project=request.getSession().getServletContext().getRealPath("/");
  		String model=project+"WEB-INF"+File.separator+"models";
  		
  		System.out.println(model);
  		
  		try {
  			String upload;
  			String finalLabel;
  			
  			//将输入流接受成字符串形式
  			upload = convertStreamToString(in,"UTF-8",length);
  			//调用LabelImage的静态方法excute得到概率最大的标签
  			finalLabel=LabelImage.excute(upload,model);
  			System.out.println(finalLabel);
  			//将结果写回安卓端，还要修改
  			out.write(finalLabel);
  		}
  		catch(Exception e) {
  			System.out.println(e.getMessage());
  		}
  								
  		
  	}
  	
  	//将inputStream转成字符串
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
  ```

  ### 新建类LabelImage处理tensorflow模型

- 代码参考<https://github.com/tensorflow/tensorflow/blob/master/tensorflow/java/src/main/java/org/tensorflow/examples/LabelImage.java>

- 在tensorflow的github主页上给出了java调用tensorflow模型的示例，我根据自己服务器的处理图片功能对代码进行修改

- 主要处理静态方法excute如下

  ```
  //调用模型并返回概率最大的label标签
  	public static String excute(String imageFile,String model) throws IOException {
  		//tensorflow模型所在路径
  	    //输出路径进行调试
  		System.out.println(model);
  	
  	    //读入图像识别模型
  		byte [] graphDef=readAllBytesOrExit(Paths.get(model, "frozen_inference_graph.pb"));
  //		System.out.println(model);
  
  		//读入模型的分类txt文件
  		List<String> labels=readAllLinesOrExit(Paths.get(model, "test_pb.txt"));
  		
  //		//将base64编码转成byte数组
  //		BASE64Decoder decoder=new BASE64Decoder();
  		
  		//将图像的字符串转为byte数组，用于tensor的输出
  		byte[] imageBytes=imageFile.getBytes();
  		
  		//将图像输出为tensor
  		try (Tensor<Float> image=constructAndExecuteGraphToNormalizeImage(imageBytes)) {
  			float[] labelProbablities=executeInceptionGraph(graphDef,image);
  			int bestLabelIdx=maxIndex(labelProbablities);
  			String bestLabel=labels.get(bestLabelIdx);
  			
              //输出结果验证模型准确性
  			System.out.println(
  					String.format("Best Match: %s (%.2f%% likely)",
  							labels.get(bestLabelIdx),
  							labelProbablities[bestLabelIdx]*100f));
  			//将label返回给servlet
  			return bestLabel;
  
  		}
  ```

  