    $('#reserve-now').on('show.bs.modal', function (event) {
     var modal = $(this)
     modal.find('.modal-title').text('')
     modal.find('#product-image').attr("src",'')
     modal.find('.card-text').text('')
     modal.find('#saleprice-modal').text('')
      modal.find('.notify-badge').text('')
  var button = $(event.relatedTarget) // Button that triggered the modal

  var recipient = button.data('send-to-modal')
 //alert(recipient)
 var ajaxUrl = '/product/'+recipient
$.ajax({
        url: ajaxUrl
    }).then(function(data) {
   // alert(data)
      modal.find('.modal-title').text(data.name)
      modal.find('.card-text').text(data.description)
      modal.find('#product-image').attr("src",data.primaryImage)
      modal.find('#saleprice-modal').text(data.salePrice)
      modal.find('#listprice-modal').text(data.listPrice)
      modal.find('.notify-badge').text('$ '+data.salePrice)
      modal.find('#productId').val(data.id)



   //   modal.find('.modal-body input').val(recipient)
    });

  //{"id":"5b15d3980f158b0004d9095a","name":"Kothu Parota","description":"Kothu parotta is a delicacy popular in Virudhunagar, the South Indian state of Tamil Nadu. It is made using parotta, egg, meat, and salna, a spicy sauce. Other variants of kothu parotta are Muttai kothu parotta, chilli parotta","primaryImage":"http://res.cloudinary.com/dlbwgbupa/image/upload/v1528157080/eqzqaavz0owsmhl2ikyi.jpg","additionalImages":null,"totalQuantity":100,"maxQuantityPerCustomer":2,"listPrice":10.0,"salePrice":2.0,"discount":"80.0%","tags":["Food"],"startTime":"2018-06-04T09:00:00.000+0000","endTime":"2018-10-31T19:00:00.000+0000","numberOfViews":0,"publicImageId":"eqzqaavz0owsmhl2ikyi"}
  // Extract info from data-* attributes
  // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
  // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.


})

$( document ).ready(function() {
    $("#reserveOrder").click(function (event) {
        var url='/reserve';
        $.ajax({
                   type: "POST",
                   url: url,
                   data: $("#reserveOrderForm").serialize(), // serializes the form's elements.
                   success: function(data)
                   {
                       if(data.orderPlaced)
                       {
                            alert('success');
                            $("#reserveOrder").css('background-color','#008000');
                            $("#reserveOrder").html('Reservation Complete');
                            $("#reserveOrder").prop('disabled',true);
                            $("#reserve-response").html('Congratulation! Please use this reservation code during pickup : '+data.reservationOrder.reservationCode);
                       }
                       else
                       {
                            alert('error');
                       }
                   }
                 });

            event.preventDefault();
    });
})