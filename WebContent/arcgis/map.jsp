<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<HTML>
 <HEAD>
  <TITLE>危废地位监控</TITLE>
  <meta charset="utf-8">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <META NAME="Author" CONTENT="">
  <META NAME="Keywords" CONTENT="">
  <META NAME="Description" CONTENT="">
  <link rel="stylesheet" type="text/css" href="css/common.css"/>
  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/buttons.css"/>
  <link rel="stylesheet" type="text/css" href="css/font-awesome.min.css">
  <link rel="stylesheet" type="text/css" href="http://127.0.0.1:9016/arcgis_js_api/library/3.20/3.20/dijit/themes/tundra/tundra.css"/>
  <link rel="stylesheet" type="text/css" href="http://127.0.0.1:9016/arcgis_js_api/library/3.20/3.20/esri/css/esri.css" />
  <script type="text/javascript" src="js/jquery.min.js"></script>
  <script type="text/javascript" src="js/bootstrap.min.js"></script>
  <script type="text/javascript" src="http://127.0.0.1:9016/arcgis_js_api/library/3.20/3.20/init.js"></script>
 </HEAD>
 <body style="">
    <div style="width:auto; height:15%; ">
	   <div class="myLogo">
	   		<img alt="天津市环保局" src="img/logo.png"/>
	   </div>
	   <div class="myDiv">
	   	<form role="form" class="myForm">
		   	<table class="table myTable">
	  			<tbody>
	    			<tr>
	      				<td style="width:1px">
	      					<div>
	      						<input type="text" class="form-control input-large myInput" id="name" placeholder="请输入名称">
	      					</div>
	      				</td>
	      				<td rowspan="2">
	      					<button class="button button-action button-square button-jumbo" style="margin-left: -1%;" id="queryBtn" type="button"><i class="fa fa-search"></i></button>
	      				</td>
	    			</tr>
	    			<tr>
	      				<td style="width:1px">
	      					<div class="button-group myBtns">
						   		<button type="button" class="button button-action  button-border button-small myBtn" id="qt1" onclick="btnActive('qt1');">牌照</button>
							    <button type="button" class="button button-action  button-border button-small myBtn" id="qt2" onclick="btnActive('qt2');">产生</button>
							    <button type="button" class="button button-action  button-border button-small myBtn" id="qt3" onclick="btnActive('qt3');">运输</button>
							    <button type="button" class="button button-action  button-border button-small myBtn" id="qt4" onclick="btnActive('qt4');">处置</button>
						    </div>
	      				</td>
	    			</tr>
	  			</tbody>
			</table>
		</form>
	   </div>
	   <div class="reloadDiv">
	   		<a id="reloadBtn" class="icon reload" href="#" onmousemove="solidReload()" onmouseout="hollowReload()"></a>
	   </div>
	   <div class="closeDiv">
	   		<a id="closeBtn" class="icon closeSearch" href="#" onmousemove="solidClose()" onmouseout="hollowClose()"></a>
	   </div>
    </div>
    <br/>
    <div id="mapDiv" class="mapDiv"></div>
    <script type="text/javascript">
   	var temp = 0.1;
	require(
	[ "esri/map", "esri/urlUtils", "esri/InfoTemplate",
	  "esri/dijit/InfoWindow",
			"esri/dijit/InfoWindowLite",
			"esri/layers/FeatureLayer",
			"esri/geometry/Point",
			"esri/SpatialReference",
			"esri/symbols/SimpleMarkerSymbol",
			"esri/graphic", "dojo/on", "dojo/dom",
			"esri/dijit/Popup",
			"esri/Color", "dojo/dom-construct",
			"esri/symbols/PictureMarkerSymbol",
			"dojo/domReady!","dojo" ],
	function(Map, urlUtils, InfoTemplate,InfoWindow,
			InfoWindowLite, FeatureLayer, Point,
			SpatialReference, SimpleMarkerSymbol,
			Graphic, on, dom, Popup, Color, domConstruct,PictureMarkerSymbol) {
		urlUtils.addProxyRule({
					urlPrefix : "http://192.168.0.18:6080/arcgis/rest/services/",
					proxyUrl : "proxy.jsp"
				});
		var bounds = new esri.geometry.Extent({
			"xmin" : 485263.408,
			"ymin" : 4317522.127,
			"xmax" : 558023.970,
			"ymax" : 4355159.182,
			"spatialReference" : {
				"wkid" : 2384
			}
		});

		var myMap = new Map("mapDiv", {
			//center:[113, 37],
			extent : bounds,
			zoom : 6,
			logo : false,
			infoWindow: infoWindow
		});
		
		var infoWindow = new InfoWindow({}, domConstruct.create("div"));

		infoWindow.startup();

		var myTiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer(
				"http://192.168.0.18:6080/arcgis/rest/services/tianjin/tianjin/MapServer");
		myMap.addLayer(myTiledMapServiceLayer);
		console.log(myMap.graphics);
		
		var queryType=0;
		var carsArray = [];
		var sysDate=new Date();
		var mapId="map-"+sysDate.getFullYear()+(sysDate.getMonth()+1)+sysDate.getDate()+sysDate.getHours()+sysDate.getMinutes()+sysDate.getSeconds();
		function ShowLocation(x, y) {
			var point = getPoint(x,y);
			var picBaseUrl = "http://127.0.0.1:9016/arcgis/img/";
	        var blueSymbol = new PictureMarkerSymbol(picBaseUrl + "car.png", 32, 32).setOffset(0, 15);
	        var attr = {"Xcoord":x,"Ycoord":y,"Plant":"Mesa Mint"};
	        var infoTemplate = new InfoTemplate("车辆详细信息","数据加载中，请稍后！");

			graphic = new Graphic(point,blueSymbol,attr,infoTemplate);
			myMap.graphics.add(graphic);
			return graphic;
		}
		
		function getPoint(x,y)
		{
			var point = new Point({
				"x" : x,
				"y" : y,
				"spatialReference" : {
					"wkid" : 2384
				}
			});
			return point;
		}
		
		on(dom.byId("qt1"), "click",
		function() {
			setQueryType(1);
		});
		
		on(dom.byId("qt2"), "click",
		function() {
			setQueryType(2);
		});
		
		on(dom.byId("qt3"), "click",
		function() {
			setQueryType(3);
		});
		
		on(dom.byId("qt4"), "click",
		function() {
			setQueryType(4);
		});
		
		on(dom.byId("queryBtn"), "click",
		function() {
			queryInfo();
		});
		
		on(dom.byId("closeBtn"), "click", function(pointEvent) {
			myMap.graphics.clear();
			carsArray=[];
			pData='{"key":"4","sessionId":"'+mapId+'"}';
			ws.send(pData);
		});
		
		on(dom.byId("reloadBtn"), "click", function(pointEvent) {
			setQueryType(0);
			queryInfo();
		});
		
		function setQueryType(type)
		{
			queryType=type;
		}
		
		var ws = new WebSocket("ws://localhost:9016/mywebsocket?sessionId="+mapId);
		  ws.onmessage=function(event){
			var carPositions=JSON.parse(event.data);
			if(carPositions.list == null || carPositions.list == "" || carPositions.list == "null") return;
			if(carPositions.actionType == 1)
			{
				var carPosition=carPositions.list[0];
				var location=carPosition.location;
				location=JSON.parse(location);
				if(!isHas(carPosition.uuId))
				{
					
					carPosition.gpc=ShowLocation(location.latitude,location.longitude);
					carsArray.push(carPosition);
				}
				myMap.centerAt(getPoint(location.latitude,location.longitude));
			}
			else if(carPositions.actionType == 5)
			{
				var showStr;
				carPositions.list.forEach(function(carPosition, index)
				{
					if(index == 0){
						showStr="牌照号："+carPosition.permitNum+"<br/>联单编号："+carPosition.tbId+"<br/>联单状态："+carPosition.statusName+"<br/>产生单位名称："+carPosition.enNameCs+"<br/>运输单位名称："+carPosition.enNameYs+"<br/>处置单位名称："+carPosition.enNameCz+"<br/>";
					}
					showStr=showStr+"废物类别："+carPosition.samllCategoryId+"<br/>废物名称："+carPosition.dName+"<br/>废物数量："+carPosition.unitNum+"<br/>计量单位："+carPosition.unit+"<br/>";
				});
				var infoTemplate = new InfoTemplate("车辆详细信息",showStr);
				var entity=getEntity(carPositions.list[0].uuId);
				entity.gpc.setInfoTemplate(infoTemplate);
			}
			else
			{
				carPositions.list.forEach(function(carPosition, index)
				{
					var location=carPosition.location;
					location=JSON.parse(location);
					if(isHas(carPosition.uuId))
					{
						
						var gpc=getEntity(carPosition.uuId).gpc;
						gpc.geometry.x=location.latitude;
						gpc.geometry.y=location.longitude;
						gpc.draw();
					}
					else
					{
						carPosition.gpc=ShowLocation(location.latitude,location.longitude);
						carsArray.push(carPosition);
					}
				});
			}
		  };
		  
		  ws.onerror=function(event) {
		    console.log('connection Error', event);
		  };
		  
		  ws.onclose=function(event) {
		    console.log('connection closed', event);
		  };
		  
		  ws.onopen=function() {
		    console.log('connection open');
		  };
		  
		  function checkQuery()
		  {
			  var qc=dom.byId("name").value;
			  if(qc.length==0)
				{
					alert("请输入查询条件！");
					return true;
				}
			  if(qc=="天津" || qc=="公司" || qc=="天津公司")
				{
					alert("查询条件不够精准，请重试！");
					return true;
				}
			  return false;
		  }
		  
		  function isHas(val)
		  {
		  	for(var i=0;i<carsArray.length;i++)
	  		{
		  		if(val == carsArray[i].uuId) 
	  			{
	  				return true;
	  			}
	  		}
		  	return false;
		  }
		  
		  function getEntity(val)
		  {
			  for(var i=0;i<carsArray.length;i++)
		  		{
			  		if(val == carsArray[i].uuId) 
		  			{
		  				return carsArray[i];
		  			}
		  		}
		  	return null;
		  }
		  
		  function delEntity(val)
		  {
			  for(var i=0;i<carsArray.length;i++)
		  		{
			  		if(val == carsArray[i].uuId) 
		  			{
			  			carsArray.splice(i,1);
			  			return true;
		  			}
		  		}
			  return false;
		  }
		  
		  function queryInfo()
		  {
		  	var pData;
			var pVal=dom.byId("name").value;
			var mName;
			if(queryType == 0)
			{
				pData='{"key":"2","mName":"queryPositions","sessionId":"'+mapId+'"}';
			}
			else 
			{
				if(checkQuery()) return;
				if(queryType == 1)
				{
					mName="queryPositionByLp";
				}
				else if(queryType == 2)
				{
					mName="queryPositionByCs";
				}
				else if(queryType == 3)
				{
					mName="queryPositionByYs";
				}
				else if(queryType == 4)
				{	
					mName="queryPositionByCz";
				}
				
				pData='{"key":"2","mName":"'+mName+'","qc":"'+pVal+'","sessionId":"'+mapId+'"}';
			}
			ws.send(pData);
			if(queryType != 1)
			{
				myMap.graphics.clear();
				carsArray=[];
			}
		  }
		  
		  myMap.on("load", function() {//图形鼠标点击响应事件
	          myMap.graphics.on("click",function(e){
        	  	//e.stopPropagation();
	            var node = e.graphic;
	            var tbId;
	            for(var i=0;i<carsArray.length;i++)
		  		{
			  		if(node == carsArray[i].gpc) 
		  			{
			  			tbId=carsArray[i].tbId;
			  			break;
		  			}
		  		}
	            pData='{"key":"3","mName":"queryBillInfo","tbId":"'+tbId+'","sessionId":"'+mapId+'"}';
				ws.send(pData);
	          });
	          queryInfo();
		  });
		  
	});

	function solidClose(){
		document.getElementById("closeBtn").style.background="url(img/solidClose.png) no-repeat 0px 0px";
		document.getElementById("closeBtn").style.backgroundSize = "40px 40px";
	}

	function hollowClose(){
		document.getElementById("closeBtn").style.background="url(img/close.png) no-repeat 0px 0px";
		document.getElementById("closeBtn").style.backgroundSize = "40px 40px";
	}
	
	function solidReload(){
		document.getElementById("reloadBtn").style.background="url(img/solidReload.png) no-repeat 0px 0px";
		document.getElementById("reloadBtn").style.backgroundSize = "40px 40px";
	}

	function hollowReload(){
		document.getElementById("reloadBtn").style.background="url(img/reload.png) no-repeat 0px 0px";
		document.getElementById("reloadBtn").style.backgroundSize = "40px 40px";
	}

	function btnActive(btnId){
		document.getElementById("name").focus();
		for(var i=1; i<5; i++){
			var btnIdName = "qt"+i;
			if(btnId == btnIdName){
				document.getElementById(btnIdName).style.background="#A5DE37";
				document.getElementById(btnIdName).style.color="#ffffff";
			}else{
				document.getElementById(btnIdName).style.background="#ffffff";
				document.getElementById(btnIdName).style.color="#A5DE37";
			}
		}
	}
	
  </script>
  </body>
</HTML>