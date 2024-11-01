<template>
    <div class="comment-panel-comment-list-comment-item" :odd="odd">
        <h4>
            {{ comment.user.name }}:
        </h4>
        <span>
            {{ comment.text }}
        </span>
    </div>
</template>

<script>
export default {
    name: 'CommentPanelCommentListCommentItem',
    props: {
        comment: {
            type: Object,
            required: true
        },
        odd: {
            type: Boolean,
            required: true
        }
    }
}
</script>

<style lang="scss" scoped>
.comment-panel-comment-list-comment-item {
    display: flex;
    flex-direction: column;
    gap: 5px;
    padding: 20px;

    &[odd = true] {
        background-color: #181818;
    }
}

h4 {
    margin: 0;
    color: #00FF7F;
}
</style>