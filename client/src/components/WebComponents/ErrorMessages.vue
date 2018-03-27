<template>
<div>
      <div class="alert alert-warning alert-dismissible fade show" role="alert">
    <strong class="text-danger">Error:</strong><strong>{{this.Error}}</strong>
  </div>
    </div>
</template>
  
<script>
export default {
  props: ['Error']
}
</script>
