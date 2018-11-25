<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>  
	#fileupload{ position:absolute;width:83px;height:40px; z-index:100;  font-size:60px;opacity:0;filter:alpha(opacity=0); margin-top:-5px;}  
</style> 
<!-- Modal -->
<div class="modal fade" id="picModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body" id="modal_val" style="width: 100%">
	            <input type="file" id="fileupload" name="fileuploaddata" onchange="$('#imgUploadId').attr('disabled',false);" size="1" style="position: absolute;top: 25px;left: 15px;width: 80px;height: 30px;overflow: hidden;line-height: 99em;"><button type="button" class="btn btn-default" >选择文件</button>
		        <button type="button" class="btn btn-primary" id="imgUploadId" onclick="ajax_upload();" disabled="disabled">上传</button>
		    	<div id="filepath"></div>
		    	<br/>
		    	<img id="diy" src="" width="80%" hidden="true">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
