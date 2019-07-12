
//动态添加显示树图行政区
var DistrictCodeArray = [];
$.ajax({
    url: "http://202.114.41.165:8080/FYProject/servlet/GetAllDistrict",
    type: 'get',
    dataType: 'json',
    success: function (response) {
        var result = response.result;
        //console.log(result);
        for (var i = 1; i < result.length; i++) {
            var elem_li = document.createElement('li');
            elem_li.id = result[i].DistrictCode;
            //elem_li.className = 'children children-lv2';
            document.getElementById('City').appendChild(elem_li);

            //各个地级市的最外层控件
            var elem_a = document.createElement('a');
            elem_a.className = 'CityName';
            elem_li.appendChild(elem_a);

            //添加各个地级市名称，添加点击事件
            var elem_span_name = document.createElement('span');
            //elem_span_name.id = result[i].DistrictCode;
            elem_span_name.style.cursor = 'pointer';
            elem_span_name.innerHTML = result[i].District;
            elem_span_name.onclick = function (event) {
                var span_target = event.target;
                var id = span_target.parentNode.parentNode.id;
                //console.log(span_target.innerHTML);
                zoomToSelectRegion(id);
            }
            elem_a.appendChild(elem_span_name);

            //添加下拉箭头的外包span控件，添加事件
            var elem_span = document.createElement('span');
            elem_span.className = 'triangle-down';
            elem_span.style.cursor = 'pointer';
            elem_a.appendChild(elem_span);
            //添加下拉箭头的img控件
            var elem_img = document.createElement('img');
            //elem_img.id = 'elem_img_' + i;
            elem_img.src = 'images/show1.png';
            elem_img.onclick = function (event) {
                var img_target = event.target;
                var id = img_target.parentNode.parentNode.parentNode.id.substring(0, 4);
                var ul_get = document.getElementById(id);
                if (ul_get.hidden == true) {
                    document.getElementById(id).hidden = false;
                }
                else {
                    document.getElementById(id).hidden = true;
                }
            }
            elem_span.appendChild(elem_img);

            var elem_ul = document.createElement('ul');
            //elem_ul.className = 'ul-leaf';
            elem_ul.id = result[i].DistrictCode.substring(0, 4);
            elem_ul.hidden = true;
            //elem_ul.style.display = 'none';
            elem_li.appendChild(elem_ul);

            DistrictCodeArray.push(result[i].DistrictCode);
        }
    }
}).then(function () {
    //console.log(DistrictCodeArray);
    for (var i = 0; i < DistrictCodeArray.length; i++) {
        $.ajax({
            url: "http://202.114.41.165:8080/FYProject/servlet/GetInfoByDistrictCodeServlet/" + DistrictCodeArray[i],
            type: 'get',
            dataType: 'json',
            success: function (response) {
                var result = response.result;
                //console.log(result);
                //console.log(result[i].DistrictCode);
                for (var j = 1; j < result.length; j++) {
                    //添加各个县级市的名称
                    var elem_li_Districts = document.createElement('li');
                    //elem_li_Districts.className = 'children children-lv2';
                    var discode = result[j].DistrictCode.substring(0, 4);
                    document.getElementById(discode).appendChild(elem_li_Districts);
                    var elem_a_Districts = document.createElement('a');
                    elem_a_Districts.className = 'CityName2';
                    elem_a_Districts.style.cursor = 'pointer';
                    elem_a_Districts.id = result[j].DistrictCode;
                    elem_a_Districts.innerHTML = result[j].District;
                    elem_a_Districts.onclick = function (event) {
                        var a_target = event.target;
                        zoomToSelectRegion(a_target.id);
                    }
                    //console.log(result[j].District);
                    elem_li_Districts.appendChild(elem_a_Districts);
                }
            }
        })
    }
});


$.ajax({
	url: "http://localhost:3000/",
	type: 'get',
	dataType: 'json',
	processData: false, // 不处理数据
	contentType: false, // 不设置内容类型
	success: function(response) {
		console.log(response);
	}
});




var serverRootUrl = 'http://202.114.41.165:8090/geoserver/';
var geoServerUrl_HeritageMap = serverRootUrl + 'HeritageMap/wms';
var geoServerUrl_Feiyiditu = serverRootUrl + 'Feiyiditu/wms';
var heritageLayerName = 'Feiyiditu:GJFY';
var heritageLayerName = 'HeritageMap:culture_heritage_pt';
var singleTypeHeritageLayerName = 'HeritageMap:SingleTypeHeritage';
//var _activeMapType = "project";

var singleTypeHeritageResult;
var singleTypeHeritageResultLabel;

//指定类别非遗的数据结果
var singleTypeHeritageParams = {
    LAYERS: singleTypeHeritageLayerName,
    FORMAT: 'image/png',
    VERSION: '1.1.1',
    TILED: false,
    STYLES: "",
};

var baselayer = new ol.layer.Tile({
    source: new ol.source.OSM(),
    minResolution: 0.010986328125
});
//地图加载
var view = new ol.View({
    center: [112.5, 31],
    zoom: 7,
    projection: 'EPSG:4326'
});

var map = new ol.Map({
    target: document.getElementById('map'),
    view: view,
    logo: false
});
map.addLayer(baselayer);

//地图zoom级别变化监听函数
//view.on('change:resolution', function () {
//    var resolution = view.getResolution();
//    console.log(resolution);
//});

//添加图层函数
//function addVectorLayer() {
//    var ptLayer = new ol.layer.Image({
//        source: new ol.source.ImageWMS({
//            url: geoServerUrl,
//            params: { 'LAYERS': singleTypeHeritageLayerName, 'TILED': false, 'VERSION': '1.1.1' }
//        })
//    });
//    map.addLayer(ptLayer);
//}

//添加图层函数
function addTileLayer(url, style, layer, minRe, maxRe) {
    var layer = new ol.layer.Tile({
        source: new ol.source.TileWMS({
            url: url,
            params: {
                'FORMAT': 'image/png',
                'VERSION': '1.1.1',
                TILED: false,
                STYLES: style,
                LAYERS: layer,
            }
        }),
        minResolution: minRe,
        maxResolution: maxRe
    });
    map.addLayer(layer);
    return layer;
}

//创建湖北地级市底图图层
var style_djs_param = "djs_ditu_anse";
var layer_djs_param = "Feiyiditu:prefecture_level_city";
var minRe_djs_param = 0.01;
var maxRe_djs_param = 0.0439453125;
var DJS_layer = addTileLayer(geoServerUrl_Feiyiditu, style_djs_param, layer_djs_param, minRe_djs_param, maxRe_djs_param);

//创建湖北县市底图图层
var style_xjs_param = "xjs_ditu_anse";
var layer_xjs_param = "Feiyiditu:HB_WH";
var minRe_xjs_param = null;
var maxRe_xjs_param = 0.01;
var XJS_layer = addTileLayer(geoServerUrl_Feiyiditu, style_xjs_param, layer_xjs_param, minRe_xjs_param, maxRe_xjs_param);

//创建关于黑暗传影响范围专题图
var style_influenceArea_param = "influenceArea";
var layer_influenceArea_param = "Feiyiditu:HB_WH";
var influenceArea_Layer = addTileLayer(geoServerUrl_Feiyiditu, style_influenceArea_param, layer_influenceArea_param);
influenceArea_Layer.setVisible(false);

//创建湖北县市注记图层
var style_xjslabel_param = "xjs_label";
var layer_xjslabel_param = "Feiyiditu:xjs_label";
var minRe_xjslabel_param = null;
var maxRe_xjslabel_param = 0.01;
var XJS_label = addTileLayer(geoServerUrl_Feiyiditu, style_xjslabel_param, layer_xjslabel_param, minRe_xjslabel_param, maxRe_xjslabel_param);


//创建湖北省国家非遗项目“点”图层
var style_gjfypoint_param = "culture_heritage_point";
var layer_gjfypoint_param = "Feiyiditu:cultural_heritage";
var minRe_gjfypoint_param = 0.0054931640625;
var maxRe_gjfypoint_param = 0.02197265625;
var GJFY_point = addTileLayer(geoServerUrl_Feiyiditu, style_gjfypoint_param, layer_gjfypoint_param, minRe_gjfypoint_param, maxRe_gjfypoint_param);



//创建国家非遗注记层
var style_gjfylabel_param = "culture_heritage";
var layer_gjfylabel_param = "Feiyiditu:cultural_heritage";
var minRe_gjfylabel_param = null;
var maxRe_gjfylabel_param = 0.010986328125;
var GJFY_label = addTileLayer(geoServerUrl_Feiyiditu, style_gjfylabel_param, layer_gjfylabel_param, minRe_gjfylabel_param, maxRe_gjfylabel_param);


//创建专题地图图层
var style_theme_param = "Thememap_Sum";
var layer_theme_param = "Feiyiditu:djs"
//var minRe_gjfylabel_param = null;
//var maxRe_gjfylabel_param = null;
var Themelayer_Sum = addTileLayer(geoServerUrl_Feiyiditu, style_theme_param, layer_theme_param);
Themelayer_Sum.setVisible(false);


//创建湖北地级市注记图层
var style_djslabel_param = "djs_label";
var layer_djslabel_param = "Feiyiditu:djs_label";
var minRe_djslabel_param = 0.01;
var maxRe_djslabel_param = 0.0439453125;
var DJS_label = addTileLayer(geoServerUrl_Feiyiditu, style_djslabel_param, layer_djslabel_param, minRe_djslabel_param, maxRe_djslabel_param);



//国家非遗不同类型图标层
var GJFY_img = new ol.layer.Vector({
    name: 'GJFY_img',
    source: new ol.source.Vector({
        format: new ol.format.GeoJSON(),
        url: 'http://202.114.41.165:8090/geoserver/wfs?service=wfs&version=1.1.0&request=GetFeature&typeNames=Feiyiditu:cultural_heritage&outputFormat=application/json&srsname=EPSG:4326'
    }),
    style: function (feature) {
        var type = feature.get('ItemType');
        return ImgSymbol(type);
        //console.log(ImgSymbol(type));
    },
    maxResolution: 0.0054931640625
});
map.addLayer(GJFY_img);

//非遗图标，根据不同type类型返回不同的图标样式style
function ImgSymbol(type) {
    switch (type) {
        case 1:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Minjianchuanshuo.png',
                    scale: 0.12,
                    anchor: [0.4, 1.3]
                })
            });
        case 2:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Yinyue.png',
                    scale: 0.04,
                    anchor: [0.4, 1.3]
                })
            });
        case 3:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Wudao.png',
                    scale: 0.07,
                    anchor: [0.4, 1.3]
                })
            });
        case 4:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Xiju.png',
                    scale: 0.12,
                    anchor: [0.4, 1.3]
                })
            });
        case 5:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Quyi.png',
                    scale: 0.04,
                    anchor: [0.4, 1.3]
                })
            });
        case 6:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Jiyi.png',
                    scale: 0.16,
                    anchor: [0.4, 1.3]
                })
            });
        case 7:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Meishu.png',
                    scale: 0.08,
                    anchor: [0.4, 1.3]
                })
            });
        case 8:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Tiyu.png',
                    scale: 0.15,
                    anchor: [0.4, 1.3]
                })
            });
        case 9:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Yiyao.png',
                    scale: 0.06,
                    anchor: [0.4, 1.3]
                })
            });
        case 10:
            return new ol.style.Style({
                image: new ol.style.Icon({
                    src: 'images/Minsu.png',
                    scale: 0.08,
                    anchor: [0.4, 1.3]
                })
            });
    }
}

//创建传承人图层
var InheritorLayer = new ol.layer.Vector({
    name: 'inheritor',
    source: new ol.source.Vector({
        format: new ol.format.GeoJSON(),
        url: 'http://202.114.41.165:8090/geoserver/wfs?service=wfs&version=1.1.0&request=GetFeature&typeNames=Feiyiditu:inheritor&outputFormat=application/json&srsname=EPSG:4326'
    }),
    style: new ol.style.Style({
        image: new ol.style.Icon({
            src: 'images/inheritor1.png',
            scale: 0.25,
            anchor: [0.5, 1]
        })
    }),
    maxResolution: 0.010986328125
});
map.addLayer(InheritorLayer);
InheritorLayer.setVisible(false);

//创建传承人注记图层
var InheritorLayer_zhuji = new ol.layer.Vector({
    source: new ol.source.Vector({
        format: new ol.format.GeoJSON(),
        url: 'http://202.114.41.165:8090/geoserver/wfs?service=wfs&version=1.1.0&request=GetFeature&typeNames=Feiyiditu:inheritor&outputFormat=application/json&srsname=EPSG:4326'
    }),
    style: function (feature) {
        return new ol.style.Style({
            text: new ol.style.Text({
                font: '12px 黑体',
                text: feature.get('Name'),
                stroke: new ol.style.Stroke({
                    color: 'red',
                    width: 0.3
                }),
                fill: new ol.style.Fill({
                    color: 'red'
                })
            })
        })
    },
    maxResolution: 0.010986328125
});
map.addLayer(InheritorLayer_zhuji);
InheritorLayer_zhuji.setVisible(false);


var vectorSource = new ol.source.Vector();

var vectorquery = new ol.layer.Vector({
    source: vectorSource,
    style: new ol.style.Style({
        image: new ol.style.Icon({
            src: 'images/pic.png',
            scale: 1,
            anchor: [0.4, 1.3]
        })

        //stroke: new ol.style.Stroke({
        //    color: 'rgba(255, 0, 0, 1.0)',
        //    width: 2
        //})
    })
});

//获取当前地图上的layer的名字和可见属性
function getLayerName() {
    var layerGroup = map.getLayers();
    var layers = layerGroup.getArray();
    for (var i = 0; i < layers.length ; i++) {
        var visible = layers[i].getVisible();
        layerName = layers[i].getProperties().name;
        if (visible == true) {
            if (layerName != null) {
                return layerName;
            }
        }
    }
};

//根据导航栏树图中行政区编码定位到对应区域
function zoomToSelectRegion(regionCode) {
    var layerName = getLayerName();
    if (layerName == 'GJFY_img') {
        //if(_activeMapType === "project"){
        //判断当前点击的是地级市还是区县
        //console.log(regionCode);
        var layerName = 'prefecture_level_city';
        if (regionCode.substring(4) == "00" || regionCode.substring(2, 2) == "90")
            layerName = "prefecture_level_city";
        else
            layerName = "HB_WH";

        //console.log(layerName);

        var featureRequest = new ol.format.WFS().writeGetFeature({
            srsName: 'EPSG:4326',
            featureNS: 'http://202.114.41.165:8090/geoserver/Feiyiditu',
            featureTypes: [layerName],
            outputFormat: 'application/json',
            filter: ol.format.filter.equalTo('ADMINCODE', regionCode)
        });

        fetch('http://202.114.41.165:8090/geoserver/Feiyiditu/ows', {
            method: 'POST',
            body: new XMLSerializer().serializeToString(featureRequest)
        }).then(function (response) {
            return response.json();
        }).then(function (json) {
            //console.log(json);
            var features = new ol.format.GeoJSON().readFeatures(json);
            // var geom = new ol.format.GeoJSON().readGeometry(json);
            if (features.length > 0) {
                vectorSource.clear();
                vectorSource.addFeatures(features);
                map.getView().fit(vectorSource.getExtent());
                //console.log(features);
                //console.log(features[0].getProperties());
            }
            if (layerName == 'HB_WH') {
                $('.chart-all').css('display', 'none');
                $('.table-all').css('display', 'none');
                return;
            }
            else {
                $('.chart-all').show();
                var dataTabel = RightTabelPopup(features)
                RightChartPopup(dataTabel, features);
            }
        });
    }

    if (layerName == 'inheritor') {
        //if(_activeMapType === "inheritor") {
        if (regionCode.substring(4) != "00" || regionCode.substring(2, 2) != "90") {
            var featureRequest = new ol.format.WFS().writeGetFeature({
                srsName: 'EPSG:4326',
                featureNS: 'http://202.114.41.165:8090/geoserver/Feiyiditu',
                featureTypes: ['HB_WH'],
                outputFormat: 'application/json',
                filter: ol.format.filter.equalTo('ADMINCODE', regionCode)
            });
            fetch('http://202.114.41.165:8090/geoserver/Feiyiditu/ows', {
                method: 'POST',
                body: new XMLSerializer().serializeToString(featureRequest)
            }).then(function (response) {
                return response.json();
            }).then(function (json) {
                //console.log(json);
                var features = new ol.format.GeoJSON().readFeatures(json);
                // var geom = new ol.format.GeoJSON().readGeometry(json);
                if (features.length > 0) {
                    vectorSource.clear();
                    vectorSource.addFeatures(features);
                    map.getView().fit(vectorSource.getExtent());
                    //console.log(features);
                    //console.log(features[0].getProperties());
                }
                $('.chart-all').css('display', 'none');
                $('.table-all').css('display', 'none');
            });
            return;
        };
        var adminname = document.getElementById(regionCode).children[0].children[0].textContent;
        //$('.chart-all').show();
        $('.table-all').show();
        $('#example').remove();
        $('.table-container').empty();
        var featureRequest = new ol.format.WFS().writeGetFeature({
            srsName: 'EPSG:4326',
            featureNS: 'http://www.geoserver.com/Feiyiditu',
            featureTypes: ['inheritor'],
            outputFormat: 'application/json',
            filter: ol.format.filter.equalTo('NativePlac', adminname)
        });
        fetch('http://202.114.41.165:8090/geoserver/Feiyiditu/ows', {
            method: 'POST',
            body: new XMLSerializer().serializeToString(featureRequest)
        }).then(function (response) {
            return response.json();
            //console.log(response.json);
        }).then(function (json) {
            //console.log(json);
            var features = new ol.format.GeoJSON().readFeatures(json);
            //console.log(features);
            //console.log(features.length);

            var nameTable = [];
            for (var i = 0; i < features.length; i++) {
                nameTable[i] = [i + 1, features[i].N.Name, features[i].N.ItemsName, features[i].N.ItemsClass, features[i].N.Level, ]
            }
            //console.log(nameTable);

            map.getView().setCenter([features[0].N.geometry.A[0], features[0].N.geometry.A[1]]);
            map.getView().setZoom(10);

            //设置弹窗的灰色title内容
            $('.ItemStyle').text(adminname);
            $('.ItemSum').text("(共" + features.length + "人）");
            $('.table-container').append('<table id="example" class="display" width="100%"></table>');
            $('#example').DataTable({
                data: nameTable,
                columns: [
                    { title: "序号" },
                    { title: "姓名" },
                    { title: "非遗名称" },
                    { title: "非遗类型" },
                    { title: "级别" }
                ]
            });
            $('#example_filter').find('label').val('搜索');
            $('th.sorting_asc').css('width', '26px');
            $('#example_length.dataTables_length').css('margin-left', '6px');
            $('#example_filter.dataTables_filter').find('label').css('width', '160px');

            //点击表格里的非遗项目，便在地图上跳转到对应位置
            $("#example tbody").on("click", "tr", function (event) {
                //获取tr下的第二个td元素的值
                //var item_name = $(this).find('td:nth-child(2)').text();
                var item_no = $(this).find('td:first').text() -
                //var geom_no = features[item_no].O.geometry;
                map.getView().setCenter([features[item_no].N.geometry.A[0], features[item_no].N.geometry.A[1]]);
            });
            isTableShown = false;

            $('.table-container').css('display', 'block');
            $('.table-title').css('display', 'block');
            $('.chart-all').css('display', 'none');
        })
    }
};

//右侧表格弹窗
function RightTabelPopup(features) {
    //$('.chart-all').show();
    //$('.table-all').show();
    $('#example').remove();
    $('.table-container').empty();

    var properties = features[0].getProperties();
    var Table = [];
    Table[0] = [1, "民间传说", properties.folklore];
    Table[1] = [2, "传统音乐", properties.trmusic];
    Table[2] = [3, "传统舞蹈", properties.trdance];
    Table[3] = [4, "传统戏剧", properties.tropera];
    Table[4] = [5, "曲艺", properties.quyi];
    Table[5] = [6, "传统技艺", properties.trskill];
    Table[6] = [7, "传统美术", properties.trart];
    Table[7] = [8, "传统体育", properties.trsport];
    Table[8] = [9, "传统医药", properties.trmedicine];
    Table[9] = [10, "民俗", properties.folkway];

    for (var i = 0; i < Table.length; i++) {
        if (Table[i][2] == 0) {
            Table.splice(i, 1);
        }
        //console.log(Table[i][1]);
        //console.log(Table[i][2]);
    }
    for (var j = 0; j < Table.length; j++) {
        Table.sort(function (y, x) {
            return x[2] - y[2];
        });
        Table[j][0] = j + 1;
    }
    //console.log(Table);
    //console.log(Table.length);

    //设置弹窗的灰色title内容
    $('.ItemStyle').text(properties.ADMINNAME);
    $('.ItemSum').text("(共" + properties.Item_Num + "）");
    $('.table-container').append('<table id="example" class="display" width="100%"></table>');
    $('#example').DataTable({
        data: Table,
        columns: [
            { title: "序号" },
            { title: "非遗类别" },
            { title: "数量" }
        ]
    });
    $('#example_filter').find('label').val('搜索');
    $('th.sorting_asc').css('width', '26px');
    $('#example_length.dataTables_length').css('margin-left', '6px');
    $('#example_filter.dataTables_filter').find('label').css('width', '160px');

    $("#example tbody").on("click", "tr", function (event) {
        //获取tr下的第二个td元素的值
        //var item_name = $(this).find('td:nth-child(2)').text();
        var item_no = $(this).find('td:first').text() - 1;
        //console.log(features);
        var geom_no = features[item_no].O.geometry;
        map.getView().setCenter([geom_no.A[0], geom_no.A[1]]);
    });
    isTableShown = false;
    return Table;
}

//右侧下面统计图表弹窗
function RightChartPopup(dataTabel, features) {
    $('.table-all').css('display', 'none');
    var chartdata = new Array();
    for (var i = 0; i < dataTabel.length; i++) {
        if (dataTabel[i][2] != 0) {
            chartdata.push({ "value": dataTabel[i][2], "name": dataTabel[i][1] });
        }
    }
    //console.log(chartdata);

    //利用Echart创建饼图统计
    var myChart = echarts.init(document.getElementById('main'));
    option = {
        backgroundColor: '#FFFFFF',
        tooltip: {
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        //图例
        legend: {
            bottom: 5,
            left: 'center',
            data: ['民间传说', '传统音乐', '传统舞蹈', '传统戏剧', '曲艺', '传统技艺', '传统美术', '传统体育', '传统医药', '民俗']
        },
        //数据设置
        series: [
            {
                name: features[0].getProperties().ADMINNAME,
                type: 'pie',
                radius: '65%',
                center: ['50%', '40%'],
                selectedMode: 'single',
                data: chartdata,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    myChart.setOption(option);


    //点击“查看数据”按钮，实现窗口的打开和关闭
    var flag = true;
    $('.btnData').click(function myfunction() {
        if (flag) {
            $('.table-all').css('display', 'block');
            //$('.table-container').css('display', 'block');
            //$('.table-title').css('display', 'block');
        }
        else {
            $('.table-all').css('display', 'none');
        }
        flag = !flag;
    });

    //控制表格弹窗的显示
    $('.chart-title').css('display', 'block');
    $('.chart-container').css('display', 'block');
    $('.chartItemStyle').text(features[0].N.ADMINNAME);
    $('.chartItemSum').text("非遗比例");
    $('.chart-close').click(function () {
        $('.chart-all').hide();
    });
}

//根据非遗项目类别过滤显示
$('.CategoryName').click(function filterHeritageByType(event) {
    map.getView().setZoom(8);
    map.getView().setCenter([112, 31]);
    var a = event.target;
    var heritageType = a.id;
    map.removeLayer(singleTypeHeritageResult);
    map.removeLayer(singleTypeHeritageResultLabel);

    //点击非遗项目类别根节点时显示全部非遗项目
    //if (heritageType == '0') {
    //    GJFY_point.setVisible(true);
    //    GJFY_label.setVisible(true);
    //    GJFY_img.setVisible(true);
    //    return;
    //}

    //根据非遗项目类别进行过滤
    var viewparams = [
        'itemtype:' + heritageType,
    ];
    //console.log(viewparams);
    singleTypeHeritageParams.viewparams = viewparams.join(';');

    singleTypeHeritageParams.STYLES = 'postGIS';
    //console.log(singleTypeHeritageParams);

    singleTypeHeritageResult = new ol.layer.Tile({
        name: 'heritage',
        source: new ol.source.TileWMS({
            url: geoServerUrl_HeritageMap,
            params: singleTypeHeritageParams
        })
    });
    map.addLayer(singleTypeHeritageResult);


    //singleTypeHeritageParams.STYLES = 'postGIS_label';
    singleTypeHeritageResultLabel = new ol.layer.Tile({
        name: 'heritagelabel',
        source: new ol.source.TileWMS({
            url: geoServerUrl_HeritageMap,
            params: {
                LAYERS: singleTypeHeritageLayerName,
                FORMAT: 'image/png',
                VERSION: '1.1.1',
                TILED: false,
                STYLES: "postGIS_label",
                viewparams: 'itemtype:' + heritageType,
            }
        })
    });

    map.addLayer(singleTypeHeritageResultLabel);

    GJFY_point.setVisible(false);
    GJFY_label.setVisible(false);
    GJFY_img.setVisible(false);
});

//根据非遗数量进行缩聚显示
function clusterPoints() {
    var clusterSource = new ol.source.Cluster({
        distance: 30,   //距离设置是关键  Minimum distance in pixels between clusters. Default is 20
        source: GJFY.getSource()
    });

    var styleCache = {};
    var clusters = new ol.layer.Vector({
        source: clusterSource,
        style: function (feature) {
            var size = feature.get('features').length;
            //var size = 100;
            var style = styleCache[size];
            if (!style) {
                style = new ol.style.Style({
                    image: new ol.style.Circle({
                        radius: 10,
                        stroke: new ol.style.Stroke({
                            color: '#fff'
                        }),
                        fill: new ol.style.Fill({
                            color: '#3399CC'
                        })
                    }),
                    text: new ol.style.Text({
                        text: size.toString(),
                        fill: new ol.style.Fill({
                            color: '#fff'
                        })
                    })
                });
                styleCache[size] = style;
            }
            return style;
        }
    });
    map.addLayer(clusters);
}
//clusterPoints();


//实现点击非遗图标，弹出弹框功能
//组成popup的元素
var container = document.getElementById('popup');
var content = document.getElementById('popup-content');
var closer = document.getElementById('popup-closer');
var element = document.getElementById('overlay');

var overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */({
    element: container,
    autoPan: true,
    autoPanAnimation: {
        duration: 250
    }
}));

closer.onclick = function () {
    overlay.setPosition(undefined);
    closer.blur();
    return false;
};

map.on('singleclick', function (evt) {
    var layerName;
    var feature = map.forEachFeatureAtPixel(evt.pixel,
        function (feature) {
            return feature;
        }, {
            layerFilter: function (layer) {
                layerName = getLayerName();
                //if(_activeMapType === "project"){
                if (layerName == 'GJFY_img') {
                    return layer.get('name') === 'GJFY_img';
                }
                else {
                    return layer.get('name') === 'inheritor';
                }
            },
            hitTolerance: 5
        });
    if (feature == null) {
        return;
    }
    var geom = feature.getGeometry();
    var coordinate = [geom.A[0], geom.A[1]];
    overlay.setPosition(evt.coordinate);

    AddPopupConten(feature, layerName);
});

map.addOverlay(overlay);

function AddPopupConten(feature, layerName) {
    //console.log(layerName);
    document.getElementById('Item-inher_0').innerHTML = '';
    document.getElementById('Item-inher_1').innerHTML = '';
    document.getElementById('Item-inher_2').innerHTML = '';
    document.getElementById('popup').style.display = 'block';
    //if(_activeMapType === "project"){
    if (layerName === "GJFY_img") {
        var ProjectCode = feature.getProperties().ProjectCod;
        var ItemCode = feature.getProperties().ItemCode;

        $.ajax({
            url: "http://202.114.41.165:8080/FYProject/servlet/GetItemAndInheritorsByProject/" + ProjectCode,
            type: 'get',
            dataType: 'json',
            success: function (response) {
                var inheritors = response.Inheritors;
                var inteminfo = response.Iteminfo;
                var ItemName = inteminfo[0].ItemName;
                var ItemType = inteminfo[0].Itemtype;
                var ItemPic = inteminfo[0].Pics;
                var ItemIntroduce = inteminfo[0].ItemIntroduce;
                if (ItemIntroduce.length > 55)
                    ItemIntroduce = ItemIntroduce.substring(0, 55) + "...";
                $('.popup-title').text(ItemName);
                $('#Item-type').text(ItemType);
                for (var i = 0; i < inheritors.length; i++) {
                    $('#Item-inher_' + i).html('<a target=_blank href="inherit.aspx?ID=' + inheritors[i].InheritorCode + '">' + inheritors[i].Name + '</a>');
                }
                $('#img1').attr('src', 'http://202.114.41.165:8080' + ItemPic + '');
                $('.pop-Introduce').html(ItemIntroduce);
                //$('.lookMore').html('<a style="float:center;margin-top:10px; background-color:blue; width:320px; font-size:10px" target=_blank href="detailXm.aspx?title=' + ItemCode + '">更多>></a>');
                $('#detailLink').attr('href', 'HeritageDetail.aspx?ID=' + ProjectCode);
                if (ItemName.indexOf("黑暗传") != -1) {
                    //document.getElementById('influenceArea').style.display = 'block';
                    //$("ul.popNav li.influenceArea").show();
                    $('#influenceArea').show();
                    //document.getElementById('detailLink').style.width = '45%';
                }
                else {
                    //document.getElementById('influenceArea').style.display = 'none';
                    //$("ul.popNav li.influenceArea").hide();
                    $('#influenceArea').hide();
                    //document.getElementById('detailLink').style.width = '90%';
                }
            }
        })
    }

    //if(_activeMapType === "inheritor"){
    if (layerName === "inheritor") {
        var ProjectCode = feature.getProperties().ProjectCod;
        var InheritorCode = feature.getProperties().InheritCod;

        $.ajax({
            url: "http://202.114.41.165:8080/FYProject/servlet/GetByInheritorCodeServlet/" + InheritorCode + "",
            type: 'get',
            dataType: 'json',
            processData: false,  // 不处理数据
            contentType: false,  // 不设置内容类型
            success: function (response) {
                document.getElementById('td_1').innerHTML = '级别';
                document.getElementById('td_2').innerHTML = '地址';
                //console.log(response);
                var result = response.data[0];
                //console.log(result);
                var InheritorName = result.Name;
                var InheritorLevel = result.Level;
                var InheritorAddress = result.Address;
                var InheritorPic = result.Bootstrap;
                var InheritorIntroduce = result.Resume;
                if (InheritorIntroduce == null) {
                    InheritorIntroduce = '无';
                }
                if (InheritorIntroduce.length > 80)
                    InheritorIntroduce = InheritorIntroduce.substring(0, 80) + "...";
                $('.popup-title').text(InheritorName);
                $('#Item-type').text(InheritorLevel);
                $('#Item-inher_0').text(InheritorAddress);
                $('#img1').attr('src', 'http://202.114.41.165:8080' + InheritorPic + '');
                $('.pop-Introduce').html(InheritorIntroduce);
                ////$('.lookMore').html('<a style="float:center;margin-top:10px; background-color:blue; width:320px; font-size:10px" target=_blank href="detailXm.aspx?title=' + ItemCode + '">更多>></a>');
                $('#detailLink').attr('href', 'inherit.aspx?ID=' + InheritorCode);
            }
        })
    }
};

//点击查看黑暗传的影响范围
$('#influenceArea').click(function (evt) {
    var title = document.getElementById('popup').children[1].innerText;
    if (title == '黑暗传'||title=='陈切松') {
        if (this.innerHTML.indexOf('关闭影响范围') != -1) {
            influenceArea_Layer.setVisible(false);
            $('#CloseinfluenceArea').css('display', 'none');
            //this.innerHTML = "影响范围";
            this.innerHTML = '<a  href="#">影响范围</a>';
        }
        else {
            influenceArea_Layer.setVisible(true);
            map.getView().setZoom(9);
            $('#CloseinfluenceArea').css('display', 'block');
            //this.innerHTML = "关闭影响范围";
            this.innerHTML = '<a  href="#">关闭影响范围</a>';
        }
    }
});
//点击关闭黑暗传影响区域
$('#CloseinfluenceArea').click(function () {
    influenceArea_Layer.setVisible(false);
    $(this).css('display', 'none');
    $('#influenceArea').html("影响范围");
    $('#influenceArea').html('<a  href="#">影响范围</a>');
});

//鼠标移动，要素的style样式变为小手
map.on('pointermove', function (evt) {
    if (evt.dragging) {
        return;
    }
    var pixel = map.getEventPixel(evt.originalEvent);
    var hit = map.hasFeatureAtPixel(pixel, {
        layerFilter: function (layer) {
            var layerName = getLayerName();
            if (layerName == 'GJFY_img') {
                return layer.get('name') === 'GJFY_img';
            }
            if (layerName == 'inheritor') {
                return layer.get('name') === 'inheritor';
            }
        },
        hitTolerance: 5
    });
    map.getTarget().style.cursor = hit ? 'pointer' : '';
});

//地图搜索
$(function () {
    //回车搜索
    $(document).keyup(function (event) {
        if (event.keyCode == 13) {
            SearchFunction();
        }
    });
   
    //鼠标点击搜索按钮搜索
    $(".search_ico").click(function () {
        SearchFunction();
    })
});
//地图搜索函数
function SearchFunction() {
    //$(".search_bar").toggleClass('search_open');
    var keys = $("#search").val();
    var features;
    var layerName;
    var getname = getLayerName();
    if (getname == "GJFY_img") {
        layerName = "cultural_heritage"
    }
    else {
        layerName = "inheritor";
    }

    if (keys.length > 0) {
        vectorSource.clear();
        $('#result').find('li').remove();
        var searchText = keys;
        var featureRequest = new ol.format.WFS().writeGetFeature({
            srsName: 'EPSG:4326',
            featureNS: 'http://www.geoserver.com/Feiyiditu',
            featureTypes: [layerName],
            outputFormat: 'application/json',
            filter: ol.format.filter.like('Name', '*' + searchText + '*')
        });
        fetch('http://202.114.41.165:8090/geoserver/Feiyiditu/ows', {
            method: 'POST',
            body: new XMLSerializer().serializeToString(featureRequest)
        }).then(function (response) {
            return response.json();
        }).then(function (json) {
            features = new ol.format.GeoJSON().readFeatures(json);

            if (features.length == 0) {

                var elem_li_space_head = document.createElement('li');
                elem_li_space_head.style.height = '20px';
                document.getElementById('result').appendChild(elem_li_space_head);

                var elem_li = document.createElement('li');
                elem_li.style.fontSize = '13px';
                document.getElementById('result').appendChild(elem_li);

                var elem_div = document.createElement('div');
                elem_div.style.color = ('blue');
                elem_div.innerHTML = "查无此结果，请尝试切换图层或更换关键字";
                elem_li.appendChild(elem_div);

                var elem_li_space_bottm = document.createElement('li');
                elem_li_space_bottm.style.height = '20px';
                document.getElementById('result').appendChild(elem_li_space_bottm);

                return;
                //console.log(features[0].getGeometry());
            }
            //console.log(features);

            //var geom = new ol.format.GeoJSON().readGeometry(json);

            //循环遍历动态添加搜索控件及结果
            for (var i = 0; i < features.length; i++) {

                var elem_li_space_head = document.createElement('li');
                elem_li_space_head.style.height = '20px';
                document.getElementById('result').appendChild(elem_li_space_head);

                var elem_li = document.createElement('li');
                elem_li.style.fontSize = '13px';
                elem_li.id = i;
                document.getElementById('result').appendChild(elem_li);

                var elem_div_img = document.createElement('div');
                elem_div_img.style.width = '40px';
                elem_div_img.style.height = '60px';
                elem_div_img.style.cssFloat = 'left';
                elem_div_img.innerHTML = "<img src='images/pic.png' />";
                elem_li.appendChild(elem_div_img);

                if (layerName == "cultural_heritage") {
                    var elem_span = document.createElement('span');
                    elem_span.innerHTML = "项目名称：";
                    elem_li.appendChild(elem_span);

                    var elem_span = document.createElement('span');
                    //elem_span.id = i;
                    elem_span.innerHTML = features[i].getProperties().Name;
                    elem_span.onclick = function (event) {
                        var div = event.target;
                        //console.log(event);
                        //console.log(features);
                        //console.log(i);
                        var id = div.parentNode.id;
                        var geom = features[id].getGeometry();
                        //var ItemCode = features[div.id].getProperties().ItemCode;
                        //var coordinate = [geom.A[0], geom.A[1]];
                        //overlay.setPosition(coordinate);
                        //$.ajax({
                        //    url: "http://202.114.41.165:8080/GetCulture/servlet/GetCultureServlet?choice=3&ItemId=" + ItemCode + "",
                        //    type: 'get',
                        //    dataType: 'json',
                        //    processData: false,  // 不处理数据
                        //    contentType: false,  // 不设置内容类型
                        //    success: function (response) {
                        //        var ItemName = response.result[0].Name;
                        //        var ItemType = response.result[0].Type;
                        //        var ItemInheritors = response.result[0].inheritors;
                        //        var ItemPic = response.result[0].pics;
                        //        var ItemIntroduce = response.result[0].ItemIntroduce;
                        //        if (ItemIntroduce.length > 120)
                        //            ItemIntroduce = ItemIntroduce.substring(0, 120) + "...";
                        //        $('.popup-title').text(ItemName);
                        //        $('#Item-type').text(ItemType);
                        //        $('#Item-inher').text(ItemInheritors);
                        //        $('#img1').attr('src', 'http://202.114.41.165:8080' + ItemPic + '');
                        //        $('.pop-Introduce').html(ItemIntroduce);
                        //        //$('.lookMore').html('<a style="float:center;margin-top:10px; background-color:blue; width:320px; font-size:10px" target=_blank href="detailXm.aspx?title=' + ItemCode + '">更多>></a>');
                        //        $('#detailLink').attr('href', 'HeritageDetail.aspx?ProjectCode=' + ProjectCode);
                        //    }
                        //})


                        //console.log(geom);
                        var extent = geom.getExtent();
                        map.getView().fit(extent);
                        map.getView().setZoom(10);
                    }
                    elem_span.style.cursor = 'pointer';
                    elem_span.style.color = 'blue';
                    elem_li.appendChild(elem_span);

                    var elem_div_diqu = document.createElement('div');
                    elem_div_diqu.innerHTML = "所属地区：" + features[i].getProperties().ADMINNAME;
                    elem_li.appendChild(elem_div_diqu);

                    var elem_div_inheritor = document.createElement('div');
                    elem_div_inheritor.innerHTML = "传承人：" + features[i].getProperties().Inheritors;//'<a target=_blank href="detailXm.aspx?title=' + keys + '">' + features[i].getProperties().Inheritors + '</a>';
                    elem_li.appendChild(elem_div_inheritor);
                }
                if (layerName == "inheritor") {
                    var elem_span_name = document.createElement('span');
                    //elem_div_name.style.width = '200px';
                    elem_span_name.innerHTML = "姓名：";
                    elem_li.appendChild(elem_span_name);

                    //console.log(features);

                    var elem_span_cont = document.createElement('span');
                    elem_span_cont.id = features[i].getProperties().InheritCod;
                    elem_span_cont.innerHTML = features[i].getProperties().Name;
                    elem_span_cont.style.cursor = 'pointer';
                    elem_span_cont.style.color = 'blue';
                    elem_span_cont.onclick = function (event) {
                        var sp = event.target;
                        //console.log(sp);
                        var id = sp.parentNode.id;
                        var geom = features[id].getGeometry();
                        var extent = geom.getExtent();
                        map.getView().fit(extent);
                        map.getView().setZoom(10);
                    }
                    elem_li.appendChild(elem_span_cont);


                    var elem_div_diqu = document.createElement('div');
                    elem_div_diqu.innerHTML = "所属地区：" + features[i].getProperties().NativePlac;
                    elem_li.appendChild(elem_div_diqu);

                    var elem_div_diqu = document.createElement('div');
                    elem_div_diqu.innerHTML = "传承项目：" + features[i].getProperties().ItemsName; //'<span class="diqu" style="cursor:pointer; color:blue;">' + features[i].getProperties().ItemsName + '</span>';
                    elem_li.appendChild(elem_div_diqu);

                    var elem_div = document.createElement('div');
                    elem_div.innerHTML = "级别：" + features[i].getProperties().Level;
                    elem_li.appendChild(elem_div);

                    //var elem_div_inheritor = document.createElement('div');
                    ////elem_div_inheritor.style.width = '200px';
                    //elem_div_inheritor.innerHTML = "传承人：" + '<a target=_blank href="detailXm.aspx?title=' + keys + '">' + features[i].getProperties().Inheritors + '</a>';
                    //elem_li.appendChild(elem_div_inheritor);
                }


                var elem_li_space_bottm = document.createElement('li');
                elem_li_space_bottm.style.height = '20px';
                document.getElementById('result').appendChild(elem_li_space_bottm);
            }

            var elem_li = document.createElement('li');
            elem_li.innerHTML = "清除搜索结果";
            elem_li.style.cssFloat = ('right');
            elem_li.style.color = ('blue');
            elem_li.style.fontSize = ('14px');
            elem_li.style.cursor = ('pointer');
            elem_li.onclick = function () {
                vectorSource.clear();
                $("#search").val('');
                $('#search_result').slideUp();
            }

            document.getElementById('result').appendChild(elem_li);

            vectorSource.addFeatures(features);
            map.addLayer(vectorquery);
            //console.log(vectorquery);

        });
    }
    if ($('#search_result').css('display') == 'none') {
        $('#search_result').slideDown();
    }
    else {
        if (keys.length < 1) {
            $('#search_result').slideUp();
            vectorSource.clear();
        }
    }
};

//控制不同图层的显示
$('#mainMap').click(function () {
    //_activeMapType = "project";
    map.getView().setZoom(7);
    map.getView().setCenter([112, 31]);
    GJFY_point.setVisible(true);
    GJFY_label.setVisible(true);
    GJFY_img.setVisible(true);
    InheritorLayer.setVisible(false);
    InheritorLayer_zhuji.setVisible(false);
    Themelayer_Sum.setVisible(false);
    baselayer.setVisible(true);
    DJS_layer.setVisible(true);
    XJS_layer.setVisible(true);
    XJS_label.setVisible(true);
    document.getElementById('mapTitle').style.display = 'none';
    document.getElementById('legend').style.display = 'none';
    document.getElementById('popup').style.display = 'none';
    $('.chart-all').css('display', 'none');
    $('.table-all').css('display', 'none');
    map.removeLayer(singleTypeHeritageResult);
    map.removeLayer(singleTypeHeritageResultLabel);
});

$('#inheritorMap').click(function () {
    //_activeMapType = "inheritor";
    map.getView().setZoom(8);
    map.getView().setCenter([112, 31]);
    GJFY_point.setVisible(false);
    GJFY_label.setVisible(false);
    GJFY_img.setVisible(false);
    InheritorLayer.setVisible(true);
    InheritorLayer_zhuji.setVisible(true);
    Themelayer_Sum.setVisible(false);
    baselayer.setVisible(true);
    DJS_layer.setVisible(true);
    XJS_layer.setVisible(true);
    XJS_label.setVisible(true);
    document.getElementById('mapTitle').style.display = 'none';
    document.getElementById('legend').style.display = 'none';
    document.getElementById('popup').style.display = 'none';
    $('.chart-all').css('display', 'none');
    $('.table-all').css('display', 'none');
    map.removeLayer(singleTypeHeritageResult);
    map.removeLayer(singleTypeHeritageResultLabel);
});

$('#thematicMap').click(function () {
    //_activeMapType = "thematic";
    map.getView().setZoom(8);
    map.getView().setCenter([112, 31]);
    baselayer.setVisible(false);
    GJFY_point.setVisible(false);
    GJFY_label.setVisible(false);
    GJFY_img.setVisible(false);
    InheritorLayer.setVisible(false);
    InheritorLayer_zhuji.setVisible(false);
    Themelayer_Sum.setVisible(true);
    XJS_layer.setVisible(false);
    XJS_label.setVisible(false);
    DJS_layer.setVisible(false);
    addLegend();
    document.getElementById('popup').style.display = 'none';
    $('.chart-all').css('display', 'none');
    $('.table-all').css('display', 'none');
    map.removeLayer(singleTypeHeritageResult);
    map.removeLayer(singleTypeHeritageResultLabel);
});

//各个div的显示和隐藏
$(document).ready(function () {
    $('#showContent').click(function () {
        $('.ContentBox').toggle();
    });
    $('#ByCategory').click(function () {
        map.removeLayer(singleTypeHeritageResult);
        map.removeLayer(singleTypeHeritageResultLabel);
        GJFY_point.setVisible(true);
        GJFY_label.setVisible(true);
        GJFY_img.setVisible(true);

        $('.table-all').hide();
        $('.chart-all').hide();
        $('#Category').toggle();

    });
    $('#ByCity').click(function () {
        $('.table-all').hide();
        $('.chart-all').hide();

        $('#City').toggle();
    });
    $('#SelectByItem').click(function () {
        $('#ByItem').show();
        $('#BySearch').hide();
        $('.SelectItem').css('background-color', 'darkgray');
        $('.SearchItem').css('background-color', '');
    });
});

//关闭数据表
$('.table-close').click(function () {
    $('.table-all').hide();
});

//添加专题图图例
function addLegend() {
    document.getElementById('mapTitle').style.display = 'block';

    var legend_string = ['1', '5', '8', '10', '15', '27'];
    var legend_color = ['#19774B', '#DDF196', '#FEE396', '#F57B55', '#DB443C', '#AE193B'];

    //open legend div
    document.getElementById('legend').style.display = 'block';
    //clear current content
    document.getElementById('legend_item').innerHTML = '';

    var node, textnode, labelnode;

    for (var i = 0; i < legend_string.length; i++) {
        //define li
        node = document.createElement('li');
        textnode = document.createTextNode(legend_string[i]);
        labelnode = document.createElement('span');
        labelnode.className = 'box';

        //render color
        labelnode.style.backgroundColor = legend_color[i];
        //console.log(legend_color[i]);

        node.appendChild(textnode);
        node.appendChild(labelnode);

        //console.log(node);

        //append li
        document.getElementById("legend_item").appendChild(node);

        //change title
        //document.getElementById('legend_title').innerHTML = '图例';

    }
}
