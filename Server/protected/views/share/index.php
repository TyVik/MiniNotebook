<?php $formList = $this->beginWidget('CActiveForm', array('id'=>'share-list')); ?>
<?php $formList->widget('zii.widgets.grid.CGridView', array(
	'id'=>'share-grid',
	'dataProvider'=>$model->search(),
	'summaryText'=>'',
	'selectableRows'=>20,
	'cssFile'=>Yii::app()->request->baseUrl."/css/grid.css",
	'columns'=>array(
		array(
			'class'=>'ext.Tv.TvCCheckBoxColumn',
			'id'=>'itemsSelected',
			'name'=>'Text',
        ),
		'Text',
		array(
			'class'=>'CButtonColumn',
			'template'=>'{delete}',
		),
	),
)); ?>

<?php $this->endWidget(); ?>

<div class="form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'id'=>'share-form',
	'enableAjaxValidation'=>false,
)); ?>

	<?php echo $form->errorSummary($modelAdd); ?>

	<div class="row">
		<?php echo $form->labelEx($modelAdd,'Text'); ?>
		<?php echo $form->textField($modelAdd,'Text',array('size'=>50,'maxlength'=>50)); ?>
		<?php echo $form->error($modelAdd,'Text'); ?>
	</div>

	<div class="row buttons">
		<?php echo CHtml::submitButton('Добавить'); ?>
	</div>

<?php $this->endWidget(); ?>

</div><!-- form -->