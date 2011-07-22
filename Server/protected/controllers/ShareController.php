<?php

class ShareController extends Controller
{
	/**
	 * @var string the default layout for the views. Defaults to '//layouts/column2', meaning
	 * using two-column layout. See 'protected/views/layouts/column2.php'.
	 */
	public $layout='//layouts/column1';

	/**
	 * @return array action filters
	 */
	public function filters()
	{
		return array(
			'accessControl', // perform access control for CRUD operations
		);
	}

	/**
	 * Specifies the access control rules.
	 * This method is used by the 'accessControl' filter.
	 * @return array access control rules
	 */
	public function accessRules()
	{
		return array(
			array('allow',  // allow all users to perform 'index' and 'view' actions
				'actions'=>array('index', 'getList'),
				'users'=>array('*'),
			),
		);
	}

	public function actionGetList(){
		$model=new Share('search');
		$this->renderPartial('list', array('model'=>$model));
	}

	/**
	 * Manages all models.
	 */
	public function actionIndex()
	{
		$model=new Share('search');
		$modelAdd=new Share;

		// Uncomment the following line if AJAX validation is needed
		// $this->performAjaxValidation($model);

		if(isset($_POST['itemsSelected'])){
			foreach($_POST['itemsSelected'] as $data){
				$data = chop($data);
				Yii::app()->db->createCommand('delete from Share where (Text = "'.$data.'");')->execute();
			}
		}

		if(isset($_POST['Share']))
		{
			$_POST['Share']['Text'] = chop($_POST['Share']['Text']);
			$modelAdd->attributes=$_POST['Share'];
			$modelAdd->save();
			$modelAdd->unsetAttributes();
		}

		$model->unsetAttributes();  // clear any default values
		if(isset($_GET['Share']))
			$model->attributes=$_GET['Share'];

		$this->render('index',array(
			'model'=>$model, 'modelAdd'=>$modelAdd,
		));
	}

	/**
	 * Returns the data model based on the primary key given in the GET variable.
	 * If the data model is not found, an HTTP exception will be raised.
	 * @param integer the ID of the model to be loaded
	 */
	public function loadModel($id)
	{
		$model=Share::model()->findByPk((int)$id);
		if($model===null)
			throw new CHttpException(404,'The requested page does not exist.');
		return $model;
	}
}
