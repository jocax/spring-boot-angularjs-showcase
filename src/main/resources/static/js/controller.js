imageApp.factory('ImageLoader', function ($http, $resource) {

    $http.defaults.headers.common['XYZ-TOKEN']= 'myToken';

    var ImageLoader = function () {
        this.items = [];
        this.busy = false;
        this.id = '0';
    };

    ImageLoader.prototype.loadMoreWithJsonp = function () {
        if (this.busy) {
            return;
        }
        this.busy = true;

        var url = "http://localhost:8080/service/image/" + this.id + "/?callback=JSON_CALLBACK";

        $http.jsonp(url).success(function (data) {
                //alert('Success: ' + data);
                var items = [data];
                for (var i = 0; i <= 10; i++) {
                    this.items.push(data);
                }
                this.id = this.items[this.items.length - 1].id;
                this.busy = false;
            }.bind(this)).error(function (data) {
                alert('Error: ' + data);
            });
    };


    ImageLoader.prototype.loadMoreWithHTTP = function () {
        if (this.busy) {
            return;
        }
        this.busy = true;

        var Image = $resource("http://localhost:8080/service/image/:imageId",
            {imageId: '@id'}, { image: {
                    method: 'GET',
                    isArray: false
            }});

        Image.get({imageId: this.id})
            .$promise.then(function(image) {
                var items = [image];
                for (var i = 0; i <= 10; i++) {
                    this.items.push(image);
                }
                this.id = this.items[this.items.length - 1].id;
                this.busy = false;
            }.bind(this));
    };

    return ImageLoader;
})
;


