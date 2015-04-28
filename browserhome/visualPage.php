<!DOCTYPE html>
<html>
<head>

    <meta http-equiv="X-UA-Compatible" content="chrome=1">
    <link rel="stylesheet" href="lib/as.css" type="text/css">



</head>

<body>
<div id="runPart">

<!---->
<?php
	
	$id=$_COOKIE["eventId"];
	$id=str_replace(" ","+",$id);
	$id=str_replace("|","\|",$id);
	$output = array();

	//echo  getcwd();
	//exec('/usr/java/jdk1.7.0_51/bin/java -version 2>&1', $output);
	//print_r($output);
	
	$length=20;
	$characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	$randomString = '';
	for ($i = 0; $i < $length; $i++) {
		$randomString .= $characters[rand(0, strlen($characters) - 1)];
		
	}



	echo '<p id="eventId">'.'userXML/' .$randomString.'</p>';
	echo "/usr/java/jdk1.7.0_51/bin/java -jar /home/limeng/software/alternativeEngine/getxml.jar ".$id." /home/limeng/software/alternativeEngine/Configure.txt ".$randomString." 2>&1";
	exec("/usr/java/jdk1.7.0_51/bin/java -jar /home/limeng/software/alternativeEngine/getxml.jar ".$id." /home/limeng/software/alternativeEngine/Configure.txt ".$randomString." 2>&1",$output);

	print_r($output); 
	
	//sleep(60);

?>


<!--
<p id="eventId">testMiso</p>
-->

</div>
<!-- the title part-->
<h2>Alternative splicing browser<a id="help" href="http://191.101.1.231/browserhome/help.html">help</a></h2>
	<hr></hr>
	<img src="lib/xx.jpg" name="leftPanelButton" width="20" height="20" border="0" alt="javascript button" onClick="return leftPanelExpand();" onMouseEnter="return leftPanelButtonOver();">expand the left control panel                                       Control: drag and scroll
	

	<div id="leftPanelControl" onMouseLeave="return leftPanelButtonOut();">


		<input type="checkbox" name="GenePart" value="GenePart" checked=true onchange="checkBox(this)">gene
		<input type="checkbox" name="proteinPart" value="proteinPart" checked=true onchange="checkBox(this)">protein
		<input type="checkbox" name="transcriptPart" value="transcriptPart" checked=true onchange="checkBox(this)">transcripts
		
		<br/>go to a coordinate position		
		<input type="text" name="coordinate position" id="coordiantePosition" value="">
		<input type="button" name="go" id="gotoposition" value="Go">
		<?php
			echo	"<a href='userXML/".$randomString.".xml'>Download the XML datafile.</a>";
					
		?>		
		<a href=></a>
		<br>

<!--
			<input type="checkbox" name="coordinate" value="nucliedAcid">coordinate
			<input type="checkbox" name="amino acid" value="nucliedAcid">amino acid		
			<input type="checkbox" name="exons" value="nucliedAcid">exons		
			<input type="checkbox" name="pfams" value="nucliedAcid">pfams
			<input type="checkbox" name="disprot" value="nucliedAcid">disprot
-->
		<br>
		
		
		
		
	</div>
	<p></p>

	
	<div id="mainPage">
		<p></p>
		<hr></hr><hr></hr>
		<p class="modualTitle">Exons Structure</p><br><br>

		<div id="geneVisualModule">
			<canvas id="geneVisual" width="900" height="250" tabindex='1'>
		
		</div>

		<p></p>
		<hr></hr><hr></hr>
		<p class="modualTitle">Protein Fucntions</p><br>
		<div id="proteinVisualModule1">
			<canvas id="proteinVisual1" width="900" height="500" tabindex='2'>



			
		</div>

		<p></p><hr></hr><hr></hr>
		<p class="modualTitle">Gene Structure</p><br><br>
		<div id="transcriptsVisualModule">
			<canvas id="transcriptsVisual" width="900" height="100" tabindex='3'>
		
		</div>		
		
		
		
			
	</div>
	<hr></hr>
	
	
    <script type="text/javascript" src="lib/ASPlot.js"></script>
</body>


</html>




