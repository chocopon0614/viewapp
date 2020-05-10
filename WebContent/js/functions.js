function dataArray(str) {
		var tmpResult = [];

		for (var i = 0; i < str.length; i++) {

		 var tmpData = [];
		 var tmp1 = str[i];

		  for (var k in tmp1){
		    tmpData.push(tmp1[k]);
		  }

		  tmpResult.push(tmpData);
		  
		}
		 return tmpResult;
		
};

function createLabels(data){
	var tmpLabels = [];
	    
	for (var row in data){
	   var tmp	= new Date(data[row][0]);
	   tmpLabels.push(tmp.toLocaleDateString('en-GB').slice(0,5));
    }
	
	return tmpLabels;
};


function createData1(data){
	var tmpData1 = []
	    
	for (var row in data){
	   tmpData1.push(data[row][1]);
    }

	return tmpData1;

};

function createData2(data){
	var tmpData2 = [];
	    
	for (var row in data){
	   tmpData2.push(data[row][2]);
    }

	return tmpData2;

};


function createData3(data){
	var tmpData3 = [];
	    
	for (var row in data){
	   var bmi = data[row][2]/((data[row][1]/100)*(data[row][1]/100));
	   var bmi = Math.round(bmi*100)/100;
	   tmpData3.push(bmi);

    }
	
	return tmpData3;

};