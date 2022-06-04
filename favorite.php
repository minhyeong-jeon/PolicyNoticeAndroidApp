<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받는다.
        $userID=$_POST['userID'];
        $item_name=$_POST['item_name'];
        $item_content=$_POST['item_content'];
        
        if(empty($item_name)){
            $errMSG = "항목이름이 비어있습니다.";
        }
        else if(empty($item_content)){
            $errMSG = "항목내용이 비어있습니다.";
        }
        else{
            $sql="select * from favorite where userID='$userID' and item_content='$item_content'";
            $stmt = $con->prepare($sql);
            $stmt->execute();
        
            if ($stmt->rowCount() != 0){
                echo "이미 추가된 내용입니다.\n";
            }
            else{
                if(!isset($errMSG)) // 이름과 내용 모두 입력이 되었다면 
                {
                    $sql="select * from favorite where item_name='$item_name'";
                    $stmt = $con->prepare($sql);
                    $stmt->execute();
    
                    
                // if ($item_content  != "" ){ 
                //     $sql="select * from favorite where item_content='$item_content' and item_name='$item_name'";
                //     $stmt = $con->prepare($sql);
                //     $stmt->execute();
    
                //     if ($stmt->rowCount() != 0){
                //         try{
                //             $sql = "delete from favorite where item_name='$item_name'";
                //             $stmt = $con->prepare($sql);
                //             $stmt->execute();
                           
                //             //SQL 실행결과를 위한 메시지를 생성한다.
                //             if($stmt->execute()){
                //                 $successMSG = "즐겨찾기를 삭제하였습니다.";
                //             }
                //             else{
                //                 $errMSG = "즐겨찾기 에러가 발생하였습니다.";
                //             }
                //         } catch(PDOException $e) {
                //             die("Database error: " . $e->getMessage()); 
                //         }
                //     }
                    //else{
                        try{
                            // SQL문을 실행하여 데이터를 MySQL 서버의 favorite 테이블에 저장한다. 
                            $stmt = $con->prepare('INSERT INTO favorite(userID, item_name, item_content) VALUES(:userID, :item_name, :item_content)');
                            $stmt->bindParam(':userID', $userID);
                            $stmt->bindParam(':item_name', $item_name);
                            $stmt->bindParam(':item_content', $item_content);
    
                            //SQL 실행 결과를 위한 메시지를 생성한다.
                            if($stmt->execute()){
                                $successMSG = "즐겨찾기 추가 완료";
                            }
                            else{
                                $errMSG = "즐겨찾기 추가 실패";
                            }
                        } catch(PDOException $e) {
                            die("Database error: " . $e->getMessage()); 
                        }
                    }
                }

            }
        }
    //}

            //}
        //}
    //}
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
                Name: <input type = "text" name = "name" />
                Content: <input type = "text" name = "content" />
                <input type = "submit" name = "submit" />
            </form>  
       </body>
    </html>

<?php 
    }
?>