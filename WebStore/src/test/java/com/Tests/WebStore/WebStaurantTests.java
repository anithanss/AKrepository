package com.Tests.WebStore;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.lang.Thread;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;


public class WebStaurantTests {
	
	public static void main(String[] args) {
		
		ChromeOptions chromeOptions = new ChromeOptions();
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.webstaurantstore.com/");
	    driver.findElement(By.id("searchval")).sendKeys("stainless work table");
	    driver.findElement(By.xpath("//*[@id=\"searchForm\"]/div/button")).click();
	   checkSearchResults(driver);   
		driver.close();
		driver.quit();
	}
	public static void checkSearchResults(WebDriver driver)
	{
		 List<WebElement>  pages=driver.findElements(By.xpath("//*[@id='paging']//ul/li"));
		 int pagingsize = pages.size();
		    //this will click the last page of search results
		    driver.findElement(By.xpath("//*[@id=\"paging\"]/nav/ul/li["+pagingsize+-1+"]/a")).click();
		    WebDriverWait waitforProductbox = new WebDriverWait(driver, Duration.ofSeconds(30));
		    waitforProductbox.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".product-box-container")));
		List<WebElement> itemLists=driver.findElements(By.cssSelector(".product-box-container"));
		int sizeofItems= itemLists.size();
		/*for (WebElement item : itemLists){
    	WebElement productboxDescription=item.findElement(By.tagName("span"));    	
    	Boolean check_Title= item.findElement(By.tagName("span")).getText().contains("Table");
    	if(check_Title!=true)
    	break;
    	}*/
		WebElement prodboxitem = itemLists.stream().filter(item->item.findElement(By.tagName("span")).getText().contains("Table")).findFirst().orElseThrow(null);
    	List<WebElement> AddtoCarts= driver.findElements(By.cssSelector(".btn-cart"));
		int Sizetocart=AddtoCarts.size();
		WebElement lastElement = AddtoCarts.get(Sizetocart - 1);
		//adding last search found item to cart
	    lastElement.click(); 
	    //tried to use webdrive wait little longer for the add to cart sudden popup image instead of click
	    WebDriverWait waitforsuddenpopup= new WebDriverWait(driver,Duration.ofSeconds(30));
	    waitforsuddenpopup.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".notification__content")));
	    waitforsuddenpopup.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".notification__img")));
	    
	   // driver.findElement(By.linkText("View Cart")).click();
	    
	    driver.findElement(By.xpath("//*[@id=\"cartItemCountSpan\"]")).click();
	    WebElement cartadded= driver.findElement(By.cssSelector(".details"));
		 Boolean itemadded = cartadded.isDisplayed();
		   System.out.println(itemadded);
		  
	    driver.findElement(By.cssSelector(".emptyCartButton")).click();
	    
	    //This is for Empty button under the Popup Window
	    driver.findElement(By.xpath("//*[@id='td']//div/footer/button[@type='button' and contains(., 'Empty')]")).click();
	    //waiting to see cart is empty
	    WebDriverWait waitforcartempty = new WebDriverWait(driver, Duration.ofSeconds(30));
	    waitforcartempty.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty-cart__text")));
	    Assert.assertTrue(driver.findElement(By.cssSelector(".empty-cart__text")).isDisplayed());
   
	}
	
}
