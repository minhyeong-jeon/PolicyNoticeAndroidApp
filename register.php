<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android"); //유저의 브라우저 접속환경 파악


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받는다

        $userID=$_POST['userID'];
        $hash = password_hash($_POST['userPass'],PASSWORD_BCRYPT); //$userPass가 가지고있는 비밀번호 암호화
        $userLifearray=$_POST['userLifearray'];
        $userTrgterIndvdl=$_POST['userTrgterIndvdl'];
        $userArea=$_POST['userArea'];


        //입력되지 않은 항목이 있을 경우 에러메시지 생성
        if(empty($userID)){
            $errMSG = "아이디를 입력하세요";
        }
        else if(empty($hash)){
            $errMSG = "비밀번호를 입력하세요";
        }

            //에러메시지가 정의 안되어 있다면 
        else
            if(!isset($errMSG)) //모두 입력이 되었다면 
            {
            
                $sql="select * from user where userID='$userID'";
                $stmt = $con->prepare($sql);
                $stmt->execute();
            
                if ($stmt->rowCount() == 0){
                
                    try{
                        // SQL문을 실행하여 데이터를 MySQL 서버의 user 테이블에 저장
                        $stmt = $con->prepare('INSERT INTO user(userID, userPass, userLifearray, userTrgterIndvdl, userArea) VALUES(:userID, :userPass, :userLifearray, :userTrgterIndvdl, :userArea)');
                        $stmt->bindParam(':userID', $userID);
                        $stmt->bindParam(':userPass', $hash);
                        $stmt->bindParam(':userLifearray', $userLifearray);
                        $stmt->bindParam(':userTrgterIndvdl', $userTrgterIndvdl);
                        $stmt->bindParam(':userArea', $userArea);

                        //SQL 실행결과를 위한 메시지 생성
                        if($stmt->execute())
                        {
                            $successMSG = "가입되었습니다";
                        }
                        else
                        {
                            $errMSG = "사용자 추가 에러";
                        }
        
                    } catch(PDOException $e) {
                        die("Database error: " . $e->getMessage()); 
                    }
                }
            }
            //}
        //}
        

    }

?>

<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

 $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {}
?>

<?php 
   
?>