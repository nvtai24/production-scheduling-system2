console.log("JavaScript interceptor đã được tải");

function confirmDelete(form) {
    // Hiển thị SweetAlert2 để xác nhận xóa
    Swal.fire({
        title: 'Are you sure?',
        text: "This action cannot be undone!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'No, cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            // Hiển thị thông báo thành công sau khi xác nhận và chỉ submit form khi bấm OK
            Swal.fire({
                icon: 'success',
                title: 'Deleted!',
                confirmButtonText: 'OK'
            }).then(() => {
                // Sau khi bấm OK trên thông báo thành công, tiến hành submit form
                form.submit();
            });
        }
    });
}


// Đoạn mã intercept mã lỗi 403
const originalFetch = window.fetch;

window.fetch = function (...args) {
    return originalFetch(...args)
        .then(response => {
            if (response.status === 403) {
                Swal.fire({
                    icon: 'error',
                    title: 'Access Denied',
                    text: 'You do not have permission to perform this action.',
                    confirmButtonText: 'OK'
                });
                throw new Error('Access Denied');
            }
            return response;
        })
        .catch(error => {
            console.error('Error:', error);
            throw error;
        });
};

document.addEventListener("DOMContentLoaded", function () {
    // Chọn tất cả các thẻ <a> trong trang
    const links = document.querySelectorAll("a");

    links.forEach(link => {
        link.addEventListener("click", function (event) {
            event.preventDefault(); // Ngăn chặn hành động mặc định của thẻ <a>
            const url = this.getAttribute("href");

            // Sử dụng fetch để kiểm tra quyền truy cập
            fetch(url, {
                method: 'GET', // Thay vì HEAD, sử dụng GET để đảm bảo yêu cầu được xử lý
                headers: {'X-Requested-With': 'XMLHttpRequest'} // Đánh dấu là yêu cầu AJAX
            })
                .then(response => {
                    if (response.status === 403) {
                        // Nếu không có quyền, hiển thị thông báo lỗi
                        Swal.fire({
                            icon: 'error',
                            title: 'Access Denied',
                            text: 'You do not have permission to perform this action.',
                            confirmButtonText: 'OK'
                        });
                    } else {
                        // Nếu có quyền, tiến hành điều hướng
                        window.location.href = url;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    });
});


// document.addEventListener("DOMContentLoaded", function () {
//     // Lựa chọn tất cả các form trên trang
//     const forms = document.querySelectorAll("form");
//
//     forms.forEach(form => {
//         form.addEventListener("submit", function (event) {
//             event.preventDefault(); // Ngăn chặn hành động gửi form mặc định
//             const formData = new FormData(this);
//             const action = this.getAttribute("action") || window.location.href;
//             const method = this.getAttribute("method") || "POST";
//
//             // Sử dụng fetch để kiểm tra quyền truy cập và gửi form
//             fetch(action, {
//                 method: method.toUpperCase(),
//                 headers: {'X-Requested-With': 'XMLHttpRequest'},
//                 body: formData
//             })
//                 .then(response => {
//                     if (response.status === 403) {
//                         // Nếu không có quyền, hiển thị thông báo lỗi
//                         Swal.fire({
//                             icon: 'error',
//                             title: 'Access Denied',
//                             text: 'You do not have permission to perform this action.',
//                             confirmButtonText: 'OK'
//                         });
//                         throw new Error('Access Denied'); // Dừng tiếp tục
//                     }
//                     return response.json(); // Xử lý JSON nếu có quyền
//                 })
//                 .then(data => {
//                     // Xử lý phản hồi thành công
//                     console.log('Form submitted successfully:', data);
//                     Swal.fire({
//                         icon: 'success',
//                         title: 'Success',
//                         text: 'Form submitted successfully!',
//                         confirmButtonText: 'OK'
//                     });
//                 })
//                 .catch(error => console.error('Error:', error));
//         });
//     });
// });



function clearInputs(element) {
    // Find the closest row that contains the button
    let row = element.closest('tr');

    // Find the input fields inside that row
    let quantityInput = row.querySelector('.quantity-input');
    let effortInput = row.querySelector('.effort-input');

    // Clear the input values if they exist
    if (quantityInput) {
        quantityInput.value = '';
    }

    if (effortInput) {
        effortInput.value = '';
    }
}

function toggleEdit(button) {
    // Tìm hàng hiện tại chứa nút Edit
    let row = button.closest('tr');
    let inputs = row.querySelectorAll('.edit-input');
    let displays = row.querySelectorAll('.text-display');
    let deleteButton = row.querySelector('.btn-delete'); // Nút Delete

    // Kiểm tra xem đã ở chế độ chỉnh sửa chưa
    if (!button.classList.contains('editing')) {
        // Bật chế độ chỉnh sửa: hiện các input và ẩn text hiển thị
        inputs.forEach(input => input.classList.remove('hidden'));
        displays.forEach(display => display.classList.add('hidden'));

        // Ẩn nút Delete và đổi nút Edit thành Save
        deleteButton.style.display = 'none';
        button.innerHTML = '<i class="fa-solid fa-check"></i>';
        button.classList.remove('bg-blue-500', 'btn-edit');
        button.classList.add('bg-green-500', 'editing');

        // Thêm nút Cancel
        let cancelButton = document.createElement('button');
        cancelButton.innerHTML = '<i class="fa-solid fa-xmark"></i>';
        cancelButton.classList.add('bg-red-500', 'text-white', 'px-3', 'py-1', 'rounded-md', 'text-xs', 'md:text-sm');
        cancelButton.type = 'button';

        // Khi nhấn Cancel, quay lại trạng thái ban đầu
        cancelButton.onclick = function () {
            inputs.forEach(input => input.classList.add('hidden'));
            displays.forEach(display => display.classList.remove('hidden'));
            button.innerHTML = '<i class="fa-solid fa-pen"></i>';
            button.classList.remove('bg-green-500', 'editing');
            button.classList.add('bg-blue-500', 'btn-edit');
            deleteButton.style.display = 'inline'; // Hiển thị lại nút Delete
            cancelButton.remove(); // Xóa nút Cancel
        };

        // Thêm nút Cancel vào sau nút Save
        row.querySelector('.form-action-inline').appendChild(cancelButton);

    } else {
        // Khi nhấn Save, submit các input để chỉnh sửa
        let formEdit = document.createElement('form');
        formEdit.action = '/worker/edit';
        formEdit.method = 'post';

        // Di chuyển các input từ row sang formEdit để submit
        inputs.forEach(input => formEdit.appendChild(input));

        // Thêm input chứa eid nếu chưa bị di chuyển
        let eidInput = row.querySelector('input[name="eid"]');
        if (eidInput) {
            formEdit.appendChild(eidInput);
        }

        // Thêm form vào body và submit
        document.body.appendChild(formEdit);
        formEdit.submit();
    }
}

function updateHourlyRate(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const newHourlyRate = selectedOption.getAttribute('data-salary');
    const row = selectElement.closest('tr');
    const hourlyRateDisplay = row.querySelector('.hourly-rate');
    hourlyRateDisplay.textContent = newHourlyRate + 'K';
}

// Hàm mở modal Add Plan
function openAddPlanModal() {
    fetch('/plan/add-data')
        .then(response => response.json())
        .then(data => {
            const departmentSelect = document.getElementById('department');
            departmentSelect.innerHTML = '';
            data.departments.forEach(department => {
                const option = document.createElement('option');
                option.value = department.did;
                option.textContent = department.dname;
                departmentSelect.appendChild(option);
            });

            const productTableBody = document.querySelector('#addPlanModal tbody');
            productTableBody.innerHTML = '';
            data.products.forEach(product => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td class="px-6 py-4 whitespace-nowrap">${product.pname}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <input type="text" name="quantities[${product.pid}]" placeholder="Quantity" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm">
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <input type="text" name="efforts[${product.pid}]" placeholder="Effort" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm">
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <button type="button" class="text-red-600 hover:text-red-800" onclick="clearInputs(this)">
                            <i class="fa-solid fa-xmark"></i>
                        </button>
                    </td>
                `;
                productTableBody.appendChild(row);
            });

            document.getElementById('addPlanModal').classList.remove('hidden');
            document.body.classList.add('modal-open'); // Chặn cuộn trang
        })
        .catch(error => console.error('Error loading plan data:', error));
}

function openViewPlanModal(planId) {
    fetch(`/plan/view-data?plid=${planId}`)
        .then(response => response.json())
        .then(data => {
            const planDetails = document.getElementById('production-plan-details');
            planDetails.innerHTML = `
                <p><strong>Plan ID:</strong> ${data.plan.plid}</p>
                <p><strong>Workshop:</strong> ${data.department.dname}</p>
                <p><strong>Date:</strong> ${data.plan.startdate} - ${data.plan.enddate}</p>
                <p><strong>Note:</strong> ${data.plan.note || 'No note provided'}</p> <!-- Hiển thị Note -->
                
                <h3 class="text-xl mb-4 font-semibold text-gray-700">General Plan</h3>
                <table class="min-w-full bg-white border mb-8">
                    <thead>
                        <tr class="bg-blue-500 text-center text-xs font-thin text-white">
                            <th class="p-2">Product ID</th>
                            <th class="p-2">Product Name</th>
                            <th class="p-2">Quantity</th>
                            <th class="p-2">Estimated Effort</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.productDetails.map(detail => `
                            <tr class="border-b text-center text-xs text-gray-800">
                                <td class="p-2">${detail.productId}</td>
                                <td class="p-2">${detail.productName}</td>
                                <td class="p-2">${detail.quantity}</td>
                                <td class="p-2">${detail.effort}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
                
                <h3 class="text-xl mb-4 font-semibold text-gray-700">Daily Production Plan</h3>
                <table class="min-w-full bg-white border mb-8">
                    <thead>
                        <tr class="bg-blue-500 text-center text-xs font-thin text-white">
                            <th class="p-2">Date</th>
                            <th class="p-2">Product ID</th>
                            <th class="p-2">Product Name</th>
                            <th class="p-2">Total Quantity</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.summarizedDailyProductions.map(summary => `
                            <tr class="border-b text-center text-xs text-gray-800">
                                <td class="p-2">${summary.date}</td>
                                <td class="p-2">${summary.productId}</td>
                                <td class="p-2">${summary.productName}</td>
                                <td class="p-2">${summary.totalQuantity}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;
            document.getElementById('viewPlanModal').classList.remove('hidden');
            document.body.classList.add('modal-open'); // Chặn cuộn trang
        })
        .catch(error => console.error('Error loading production plan details:', error));
}


function openEditPlanModal(planId) {
    fetch(`/plan/edit-data?plid=${planId}`)
        .then(response => response.json())
        .then(data => {
            // Nạp dữ liệu vào modal
            document.getElementById('edit-plid').value = data.plan.plid;
            document.getElementById('edit-planname').value = data.plan.plname;
            document.getElementById('edit-startdate').value = data.plan.startdate;
            document.getElementById('edit-enddate').value = data.plan.enddate;
            document.getElementById('edit-note').value = data.plan.note || ''; // Hiển thị Note cũ

            // Nạp danh sách phòng ban
            const departmentSelect = document.getElementById('edit-department');
            departmentSelect.innerHTML = ''; // Xóa các option cũ
            data.departments.forEach(department => {
                const option = document.createElement('option');
                option.value = department.did;
                option.textContent = department.dname;
                if (department.did === data.plan.departmentId) {
                    option.selected = true;
                }
                departmentSelect.appendChild(option);
            });

            // Nạp sản phẩm vào bảng
            const productTableBody = document.getElementById('edit-products-table-body');
            productTableBody.innerHTML = ''; // Xóa các hàng cũ
            data.products.forEach(product => {
                const productDetail = data.planDetails.productDetails.find(detail => detail.productId === product.pid) || {};

                const row = document.createElement('tr');
                row.innerHTML = `
                    <td class="px-6 py-4 whitespace-nowrap">${product.pname}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <input type="number" min="0" step="1" name="quantities[${product.pid}]" value="${productDetail.quantity || ''}" class="quantity-input mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm">
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <input type="number" min="0" step="0.1" name="efforts[${product.pid}]" value="${productDetail.effort || ''}" class="effort-input mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 sm:text-sm">
                    </td>
<!--                    <td class="px-6 py-4 whitespace-nowrap">-->
<!--                        <button type="button" class="text-red-600 hover:text-red-800" onclick="clearInputs(this)">-->
<!--                            <i class="fa-solid fa-xmark"></i>-->
<!--                        </button>-->
<!--                    </td>-->
                `;
                productTableBody.appendChild(row);
            });

            // Mở modal
            document.getElementById('editPlanModal').classList.remove('hidden');
            document.body.classList.add('modal-open'); // Chặn cuộn trang
        })
        .catch(error => console.error('Error loading plan data:', error));
}


function openAddWorkerModal() {
    // Thực hiện fetch trước để kiểm tra quyền truy cập và lấy dữ liệu
    fetch('/worker/add-data')
        .then(response => {
            if (!response.ok) {
                // Nếu phản hồi không thành công (ví dụ 403), chặn modal và hiển thị thông báo lỗi
                throw new Error('Access Denied');
            }
            return response.json();
        })
        .then(data => {
            // Nếu phản hồi thành công, hiển thị modal và nạp dữ liệu
            const departmentSelect = document.getElementById('department');
            const shiftSelect = document.getElementById('shift');

            if (departmentSelect && shiftSelect) {
                departmentSelect.innerHTML = '';
                data.departments.forEach(department => {
                    const option = document.createElement('option');
                    option.value = department.did;
                    option.textContent = department.dname;
                    departmentSelect.appendChild(option);
                });

                shiftSelect.innerHTML = '';
                data.salaries.forEach(salary => {
                    const option = document.createElement('option');
                    option.value = salary.sid;
                    option.textContent = `${salary.slevel} - ${salary.salary}K`;
                    shiftSelect.appendChild(option);
                });
            } else {
                console.error('Department or shift element not found in DOM');
            }

            // Hiển thị modal sau khi dữ liệu được tải
            document.getElementById('addWorkerModal').classList.remove('hidden');
            document.body.classList.add('modal-open'); // Chặn cuộn trang
        })
}


// Hàm đóng modal dùng chung
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('hidden');
        document.body.classList.remove('modal-open'); // Bỏ chặn cuộn trang
        document.body.classList.remove('overflow-hidden'); // Bỏ chặn cuộn trang
    }
}