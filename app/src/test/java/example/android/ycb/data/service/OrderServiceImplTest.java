package example.android.ycb.data.service;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import example.android.ycb.biz.service.OrderService;
import example.android.ycb.biz.model.Rate;
import example.android.ycb.data.source.remote.common.ResponseData;
import example.android.ycb.data.source.remote.order.OrderDataSource;
import io.reactivex.Single;
import org.junit.Assert;
import org.junit.Test;

public class OrderServiceImplTest {

  @Test
  public void test_publish_order_return_rate_result_success() {
    OrderDataSource orderDataSource = mock(OrderDataSource.class);
    Assert.assertNotNull(orderDataSource);
    ResponseData<Void> res = new ResponseData<>(null, 0, "success");
    when(orderDataSource.publishOrderRate(anyString(), anyInt(), anyString()))
        .thenReturn(Single.just(res));
    OrderService orderService = new OrderServiceImpl(orderDataSource);
    Assert.assertTrue(orderService.publishOrderRate("123", new Rate(5, "Hot.")).blockingGet().isSuccess());
  }

  @Test
  public void test_publish_order_return_rate_result_content_invalid() {
    OrderDataSource orderDataSource = mock(OrderDataSource.class);
    Assert.assertNotNull(orderDataSource);
    ResponseData<Void> res = new ResponseData<>(null, 1010, "content invalid");
    when(orderDataSource.publishOrderRate(anyString(), anyInt(), anyString()))
        .thenReturn(Single.just(res));
    OrderService orderService = new OrderServiceImpl(orderDataSource);
    Assert.assertFalse(orderService.publishOrderRate("123", new Rate(1, "xxx")).blockingGet().isSuccess());
    Assert.assertTrue(
        orderService.publishOrderRate("123", new Rate(1, "XXX")).blockingGet().isContentInvalidError());
  }
}