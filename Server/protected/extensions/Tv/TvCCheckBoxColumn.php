<?php
Yii::import('zii.widgets.grid.CCheckBoxColumn');

class TvCCheckBoxColumn extends CCheckBoxColumn
{
	public $createButtonLabel;
	public $createButtonImageUrl;
	public $createButtonUrl='';
	public $createButtonOptions=array('class'=>'delete');
	public $button;

	public function getHasFooter()
	{
		return true;
	}

	/**
	 * Renders the data cell content.
	 * This method renders the view, update and delete buttons in the data cell.
	 * @param integer $row the row number (zero-based)
	 * @param mixed $data the data associated with the row
	 */
	function renderFooterCellContent()
	{
		echo CHtml::imageButton(
			$this->grid->baseScriptUrl.'/delete.png',
			array('title'=>'Удалить')
		);
 	}
}
