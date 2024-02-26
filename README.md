# Microservice Spring Boot for Course

Dự án này là một microservice được xây dựng bằng Spring Boot để quản lý các khóa học trong hệ thống quản lý học tập (LMS). Nó cung cấp các chức năng để tạo, truy xuất, cập nhật và xóa các khóa học. Dịch vụ vi mô này tuân theo các nguyên tắc của kiến ​​trúc vi dịch vụ, cho phép có khả năng mở rộng, tính linh hoạt và khả năng bảo trì.


## Tính năng

 - Hoạt động CRUD của khóa học: Cho phép người dùng thực hiện các thao tác CRUD (Tạo, Đọc, Cập nhật, Xóa) trên các khóa học.
 - API RESTful: Cung cấp API RESTful để tích hợp liền mạch với các dịch vụ vi mô hoặc ứng dụng khách khác.
 - Tính bền vững của dữ liệu: Sử dụng cơ sở dữ liệu để lưu giữ thông tin khóa học.
 - Bảo mật: Thực hiện các biện pháp bảo mật để đảm bảo tính toàn vẹn dữ liệu và cơ chế xác thực/ủy quyền.

## Công nghệ được sử dụng

 - Spring Boot Starter Actuator: Giám sát và quản lý số liệu ứng dụng.
 - Spring Cloud Starter CircuitBreaker Resilience4j: Triển khai mô hình ngắt mạch để tăng khả năng phục hồi.
 - Micrometer Tracing Bridge Brave: Tích hợp tính năng theo dõi phân tán để giám sát.
 - Zipkin Reporter Brave: Báo cáo và trực quan hóa dữ liệu theo dõi được phân phối.
 - Spring Boot Starter Webflux: Xây dựng các ứng dụng web phản ứng.
 - Spring Boot Starter Data JPA: Đơn giản hóa việc truy cập dữ liệu bằng JPA.
 - Spring Boot Starter Validation: Thêm hỗ trợ xác thực cho các ứng dụng Spring Boot.
 - Spring Boot Starter Web: Starter để xây dựng ứng dụng web.
 - Spring Cloud Starter Netflix Eureka Client: Ứng dụng khách để khám phá và đăng ký dịch vụ với máy chủ Eureka.
 - Spring Boot DevTools: Công cụ phát triển ứng dụng nhanh.
 - PostgreSQL: Hệ thống quản lý cơ sở dữ liệu quan hệ.
 - Project Lombok: Cung cấp các chú thích để giảm bớt mã soạn sẵn.
 - Spring Boot Starter Test: Starter để thử nghiệm các ứng dụng Spring Boot.
 - Spring Data Redis: Tích hợp Redis với các ứng dụng Spring.
 - Lettuce Core: Máy khách Java Redis.
 - Jackson Databind: Thư viện tuần tự hóa và giải tuần tự hóa JSON.
 - Apache POI: API Java để làm việc với các tài liệu Microsoft Office.
 - JXLS JExcel: Thư viện tạo file Excel.
 - FastExcel Reader and Writer: Thư viện đọc và ghi file Excel hiệu quả.
 - OpenCSV: Thư viện đọc và ghi file CSV.
 - Spring Boot Starter Freemarker: Công cụ tạo mẫu để tạo HTML.
 - Spring Boot Starter Mail: Gửi email từ các ứng dụng Spring Boot.
 - Spring Boot Starter Security: Tính năng bảo mật cho ứng dụng Spring Boot.
 - JWT (Mã thông báo web JSON): Để triển khai xác thực và ủy quyền bằng JWT.



