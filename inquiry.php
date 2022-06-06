<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다.
        $userID=$_POST['userID'];
        $type=$_POST['type'];
        $title=$_POST['title'];
        $email=$_POST['email'];
        $content=$_POST['content'];

        if(empty($title)){
            $errMSG = "제목을 입력하세요.";
        }
        else if(empty($email)){
            $errMSG = "이메일을 입력하세요.";
        }
        else if(empty($content)){
            $errMSG = "내용을 입력하세요.";
        }

        if(!isset($errMSG)) // 이름과 나라 모두 입력이 되었다면 
        {
            $sql="select * from inquiry where userID='$userID'";
            $stmt = $con->prepare($sql);
            $stmt->execute();

            try{
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                $stmt = $con->prepare('INSERT INTO inquiry(userID, type, title, email, content) VALUES(:userID, :type, :title, :email, :content)');
                $stmt->bindParam(':userID', $userID);
                $stmt->bindParam(':type', $type);
                $stmt->bindParam(':title', $title);
                $stmt->bindParam(':email', $email);
                $stmt->bindParam(':content', $content);

                
                if($stmt->execute())
                {
                    $successMSG = "문의가 성공적으로 등록되었습니다.";
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
                Title: <input type = "text" name = "title" />
                Email: <input type = "text" name = "email" />
                Content: <input type = "text" name = "content" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>
