<?php
	$rows = Yii::app()->db->createCommand('select Text from Share')->queryColumn();
	foreach ($rows as $row){
		echo $row.'&';
	}
?>