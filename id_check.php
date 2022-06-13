<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android"); //유저의 브라우저 접속환경 파악


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받는다
        $userID=$_POST['userID'];

        //입력되지 않은 항목이 있을 경우 에러메시지 생성
        if(empty($userID)){ $errMSG = "아이디를 입력하세요"; }

        //기존에 저장된 userID를 select하여 입력된 userID와 비교
        else if ($userID!= "" ){
            //user테이블에서 userID를 조건으로하여 모든 컬럼 조회
            $sql="select * from user where userID='$userID'";  
            $stmt = $con->prepare($sql);
            $stmt->execute();
        
            if ($stmt->rowCount() != 0){    //컬럼이 존재하면 = 같은 아이디가 존재한다는 의미
                echo "이미 사용중인 아이디입니다\n";
            }
            else
            {
                echo "사용가능한 아이디입니다\n";
            }
        }
        

    }

?>

<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

 $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>
            <form action="<?php $_PHP_SELF ?>" method="POST">
                userID: <input type = "text" name = "userID" />
                userPass: <input type = "text" name = "userPass" />
                userLifearray: <input type = "text" name = "userLifearray" />
                userTrgterIndvdl: <input type = "text" name = "userTrgterIndvdl" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>